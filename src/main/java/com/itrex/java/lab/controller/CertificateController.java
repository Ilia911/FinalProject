package com.itrex.java.lab.controller;

import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.service.CertificateService;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService service;

    @GetMapping("/{userId}")
    @RolesAllowed({"CUSTOMER"})
    public ResponseEntity<List<CertificateDTO>> findAllCertificatesByUserId(@PathVariable(name = "userId") int userId) {

        List<CertificateDTO> certificates = service.findAllForUser(userId);
        return certificates != null && !certificates.isEmpty()
                ? new ResponseEntity<>(certificates, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
