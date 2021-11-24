package com.itrex.java.lab.controller;

import com.itrex.java.lab.entity.dto.ContractDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.service.ContractService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContractController {

    private final ContractService service;

    @GetMapping("/contract/{id}")
    public ResponseEntity<ContractDTO> find(@PathVariable(name = "id") int id) throws ServiceException {

        Optional<ContractDTO> contractDTO = service.find(id);

        return contractDTO.isPresent()
                ? new ResponseEntity<>(contractDTO.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/contracts")
    public ResponseEntity<List<ContractDTO>> findAll() throws ServiceException {

        List<ContractDTO> contracts = service.findAll();

        return contracts != null && !contracts.isEmpty()
                ? new ResponseEntity<>(contracts, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/contract/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "id") int id) throws ServiceException {

        boolean result = service.delete(id);

        return result ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/contract/update")
    public ResponseEntity<ContractDTO> update(@RequestBody ContractDTO contractDTO) throws ServiceException {

        ContractDTO updatedContract = service.update(contractDTO);

        return updatedContract != null
                ? new ResponseEntity<>(updatedContract, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/contract/new")
    public ResponseEntity<ContractDTO> add(@RequestBody ContractDTO contractDTO) throws ServiceException {

        Optional<ContractDTO> newContract = service.add(contractDTO);

        return newContract.isPresent()
                ? new ResponseEntity<>(newContract.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
