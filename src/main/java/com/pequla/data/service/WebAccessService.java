package com.pequla.data.service;

import com.pequla.data.entity.WebAccess;
import com.pequla.data.repository.WebAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WebAccessService {

    private final WebAccessRepository repository;

    public Page<WebAccess> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<WebAccess> getById(Integer id) {
        return repository.findById(id);
    }

    public void saveAccess(HttpServletRequest request) {
        repository.save(WebAccess.builder()
                .path(request.getServletPath())
                .address(request.getRemoteAddr() + ":" + request.getRemotePort())
                .xff(request.getHeader("X-Forwarded-For"))
                .createdAt(LocalDateTime.now())
                .build());
    }
}
