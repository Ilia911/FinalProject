package com.itrex.java.lab.controller;

import com.itrex.java.lab.entity.dto.ContractDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.service.ContractService;
import java.util.List;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService service;

    @GetMapping("/{id}")
    @RolesAllowed({"CUSTOMER", "CONTRACTOR"})
    public ResponseEntity<ContractDTO> find(@PathVariable(name = "id") int id) {

        Optional<ContractDTO> contractDTO = service.find(id);

        return contractDTO.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @RolesAllowed({"CUSTOMER", "CONTRACTOR"})
    public ResponseEntity<List<ContractDTO>> findAll() throws ServiceException {

        List<ContractDTO> contracts = service.findAll();

        return contracts != null && !contracts.isEmpty()
                ? new ResponseEntity<>(contracts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("CUSTOMER")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "id") int id) {

        boolean result = service.delete(id);

        return result ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/{id}")
    @RolesAllowed("CUSTOMER")
    public ResponseEntity<ContractDTO> update(@PathVariable(name = "id") int id, @RequestBody ContractDTO contractDTO) throws ServiceException {

        ContractDTO updatedContract = service.update(contractDTO);

        return updatedContract != null
                ? new ResponseEntity<>(updatedContract, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping
    @RolesAllowed("CUSTOMER")
    public ResponseEntity<ContractDTO> add(@RequestBody ContractDTO contractDTO) {

        Optional<ContractDTO> newContract = service.add(contractDTO);

        return newContract.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
    }
}
