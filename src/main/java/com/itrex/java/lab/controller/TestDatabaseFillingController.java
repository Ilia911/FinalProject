package com.itrex.java.lab.controller;

import com.itrex.java.lab.service.FillingDatabaseTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fill")
@RequiredArgsConstructor
public class TestDatabaseFillingController {

    private final FillingDatabaseTestService service;

    @PostMapping
    @PreAuthorize("hasAuthority('database:fill')")
    public ResponseEntity addUsers() {

        service.fillTestDatabase();
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
