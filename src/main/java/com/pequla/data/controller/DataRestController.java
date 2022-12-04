package com.pequla.data.controller;

import com.pequla.data.entity.CachedData;
import com.pequla.data.model.DataModel;
import com.pequla.data.service.CachedDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/api/data")
public class DataRestController {

    private final CachedDataService service;

    @GetMapping
    public Page<CachedData> getData(Pageable pageable, HttpServletRequest request) {
        return service.getData(pageable, request);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CachedData> getDataById(@PathVariable Integer id, HttpServletRequest request) {
        return ResponseEntity.of(service.getById(id, request));
    }

    @GetMapping(path = "/discord/{id}")
    public ResponseEntity<CachedData> getDataByDiscordId(@PathVariable String id, HttpServletRequest request) {
        return ResponseEntity.of(service.getByDiscordId(id, request));
    }

    @GetMapping(path = "/guild/{id}")
    public Page<CachedData> getDataByGuildId(@PathVariable String id, Pageable pageable, HttpServletRequest request) {
        return service.getAllByGuildId(id, pageable, request);
    }

    @GetMapping(path = "/uuid/{uuid}")
    public ResponseEntity<CachedData> getDataByUuid(@PathVariable String uuid, HttpServletRequest request) {
        return ResponseEntity.of(service.getByUuid(uuid, request));
    }

    @GetMapping(path = "/name/{name}")
    public ResponseEntity<CachedData> getDataByName(@PathVariable String name, HttpServletRequest request) {
        return ResponseEntity.of(service.getByName(name, request));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateDataWebhook(@RequestBody DataModel model) {
        service.saveData(model);
    }

    @DeleteMapping(path = "/discord/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateDataWebhook(@PathVariable String id) {
        service.deleteDataByDiscordId(id);
    }
}
