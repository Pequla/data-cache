package com.pequla.data.controller;

import com.pequla.data.entity.WebAccess;
import com.pequla.data.service.WebAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/api/access")
public class AccessRestController {

    private final WebAccessService service;

    @GetMapping
    public Page<WebAccess> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<WebAccess> getById(@PathVariable Integer id) {
        return ResponseEntity.of(service.getById(id));
    }
}
