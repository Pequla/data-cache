package com.pequla.data.service;

import com.pequla.data.entity.CachedData;
import com.pequla.data.ex.BackendException;
import com.pequla.data.model.AccountModel;
import com.pequla.data.model.DataModel;
import com.pequla.data.model.UserModel;
import com.pequla.data.repository.CachedDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CachedDataService {

    private final BackendService backendService;
    private final WebAccessService accessService;
    private final CachedDataRepository repository;

    public Page<CachedData> getData(Pageable pageable, HttpServletRequest request) {
        accessService.saveAccess(request);
        return repository.findAll(pageable);
    }

    public Optional<CachedData> getById(Integer id, HttpServletRequest request) {
        // Update cache
        Optional<CachedData> optional = repository.findById(id);
        if (optional.isEmpty()) {
            updateDataById(id);
            return Optional.empty();
        }

        // Is cache expired
        CachedData data = optional.get();
        LocalDateTime now = LocalDateTime.now();
        if (data.getCachedAt().plusHours(1).isBefore(now)) {
            log.info("Cache expired for data id " + data.getId());
            updateDataById(id);
        }

        // Respond
        accessService.saveAccess(request);
        return repository.findById(id);
    }

    public Optional<CachedData> getByDiscordId(String discordId, HttpServletRequest request) {
        validateCache(repository.findByDiscordId(discordId));
        accessService.saveAccess(request);
        return repository.findByDiscordId(discordId);
    }

    public Page<CachedData> getAllByGuildId(String guildId, Pageable pageable, HttpServletRequest request) {
        accessService.saveAccess(request);
        return repository.findAllByGuildId(guildId, pageable);
    }

    public Optional<CachedData> getByUuid(String uuid, HttpServletRequest request) {
        validateCache(repository.findByUuid(uuid));
        accessService.saveAccess(request);
        return repository.findByUuid(uuid);
    }

    public Optional<CachedData> getByName(String name, HttpServletRequest request) {
        validateCache(repository.findByNameIgnoreCase(name));
        accessService.saveAccess(request);
        return repository.findByNameIgnoreCase(name);
    }

    private void validateCache(Optional<CachedData> optional) {
        if (optional.isEmpty()) return;

        // Is cache expired
        CachedData data = optional.get();
        LocalDateTime now = LocalDateTime.now();
        if (data.getCachedAt().plusHours(1).isBefore(now)) {
            log.info("Cache expired for data id " + data.getId());
            updateDataById(data.getId());
        }
    }

    private void updateDataById(Integer id) {
        try {
            DataModel model = backendService.getData(id);

            // Exists for UUID
            if (repository.existsByUuid(model.getUuid()))
                repository.deleteByUuid(model.getUuid());

            // Exists for Discord ID
            String discord = model.getUser().getDiscordId();
            if (repository.existsByDiscordId(discord))
                repository.existsByDiscordId(discord);

            saveDataCommon(model);
        } catch (BackendException e) {
            if (e.getStatus() == 404) {
                log.error("No data for id " + id + " found on backend");
                if (repository.existsById(id)) repository.deleteById(id);
                return;
            }
            log.error("Something is wrong with backend");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void saveDataCommon(DataModel model) throws IOException, InterruptedException {
        UserModel user = backendService.getUser(model.getUuid());
        AccountModel account = backendService.getAccount(model.getUuid());
        repository.save(CachedData.builder()
                .id(model.getId())
                .name(account.getName())
                .uuid(account.getId())
                .discordId(user.getId())
                .tag(user.getName())
                .avatar(user.getAvatar())
                .guildId(model.getGuild().getDiscordId())
                .createdAt(model.getCreatedAt())
                .cachedAt(LocalDateTime.now())
                .build());
    }

    public void saveData(DataModel model) {
        try {
            log.info("Upstream: Saving cache for id " + model.getId());
            saveDataCommon(model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void deleteDataByDiscordId(String id) {
        log.info("Upstream: Removing cache for user " + id);
        repository.deleteByDiscordId(id);
    }
}
