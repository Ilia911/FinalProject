package com.itrex.java.lab.controller;

import com.itrex.java.lab.entity.dto.RoleDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.service.RoleService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> findAllCertificates() throws ServiceException {

        List<RoleDTO> roles = service.findAll();

        return roles != null && !roles.isEmpty()
                ? new ResponseEntity<>(roles, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<RoleDTO> find(@PathVariable(name = "id") int id) throws ServiceException {
        Optional<RoleDTO> roleDTO = service.find(id);

        return roleDTO.isPresent()
                ? new ResponseEntity<>(roleDTO.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
