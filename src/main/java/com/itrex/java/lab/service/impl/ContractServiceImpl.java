package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.dto.ContractDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.ContractRepository;
import com.itrex.java.lab.service.ContractService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository repository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<ContractDTO> find(int id) throws ServiceException {
        ContractDTO contractDTO = null;
        try {
            Optional<Contract> contract = repository.find(id);
            if (contract.isPresent()) {
                contractDTO = convertContractIntoContractDTO(contract.get());
            }
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return Optional.ofNullable(contractDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractDTO> findAll() throws ServiceException {
        List<ContractDTO> contractDTOs = new ArrayList<>();
        try {
            List<Contract> contracts = repository.findAll();
            if (contracts.size() > 0) {
                contractDTOs = contracts.stream().map(this::convertContractIntoContractDTO).collect(Collectors.toList());
            }
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return contractDTOs;
    }

    @Override
    @Transactional
    public boolean delete(int id) throws ServiceException {
        boolean result;
        try {
            result = repository.delete(id);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    @Transactional
    public ContractDTO update(Contract contract) throws ServiceException {
        ContractDTO updatedContractDTO = null;
        try {
            Contract updatedContract = repository.update(contract);
            if (updatedContract != null) {
                updatedContractDTO = convertContractIntoContractDTO(updatedContract);
            }
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return updatedContractDTO;
    }

    @Override
    @Transactional
    public Optional<ContractDTO> add(Contract contract) throws ServiceException {
        ContractDTO newContractDTO = null;
        try {
            Optional<Contract> newContract = repository.add(contract);
            if (newContract.isPresent()) {
                newContractDTO = convertContractIntoContractDTO(newContract.get());
            }
        } catch (RepositoryException exception) {
            exception.printStackTrace();
        }
        return Optional.ofNullable(newContractDTO);
    }

    private ContractDTO convertContractIntoContractDTO(Contract contract) {
        return modelMapper.map(contract, ContractDTO.class);
    }
}
