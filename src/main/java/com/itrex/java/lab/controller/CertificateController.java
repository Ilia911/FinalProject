package com.itrex.java.lab.controller;

import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.service.CertificateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService service;

    @GetMapping("/certificates/{userId}")
    public ResponseEntity<List<CertificateDTO>> findAllCertificatesByUserId(@PathVariable(name = "userId") int userId)
            throws ServiceException {

        List<CertificateDTO> certificates = service.findAllForUser(userId);
        return certificates != null && !certificates.isEmpty()
                ? new ResponseEntity<>(certificates, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
