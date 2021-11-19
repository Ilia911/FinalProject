package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.dto.ContractDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.ContractRepository;
import com.itrex.java.lab.repository.OfferRepository;
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

    private final ContractRepository contractRepository;
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<ContractDTO> find(int id) throws ServiceException {
        ContractDTO contractDTO = null;
        try {
            Optional<Contract> contract = contractRepository.find(id);
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
            List<Contract> contracts = contractRepository.findAll();
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
        try {
            List<Offer> offers = offerRepository.findAll(id);
            for (Offer offer : offers) {
                offer.removeContract();
                offerRepository.update(offer);
            }
            return contractRepository.delete(id);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public ContractDTO update(ContractDTO contractDTO) throws ServiceException {
        try {
            Optional<Contract> optionalContract = contractRepository.find(contractDTO.getId());
            if (optionalContract.isPresent()) {
                Contract contract = optionalContract.get();
                if (contractDTO.getDescription() != null) {
                    contract.setDescription(contractDTO.getDescription());
                }
                if (contractDTO.getStartDate() != null) {
                    contract.setStartDate(contractDTO.getStartDate());
                }
                if (contractDTO.getEndDate() != null) {
                    contract.setEndDate(contractDTO.getEndDate());
                }
                if (contractDTO.getStartPrice() != null) {
                    contract.setStartPrice(contractDTO.getStartPrice());
                }
                return convertContractIntoContractDTO(contractRepository.update(contract));
            } else {
                throw new ServiceException(String.format("Contract with id = %d does not exist", contractDTO.getId()));
            }
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public Optional<ContractDTO> add(ContractDTO contract) throws ServiceException {
        ContractDTO newContractDTO = null;
        try {
            Optional<Contract> newContract = contractRepository.add(convertContractDTOIntoContract(contract));
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

    private Contract convertContractDTOIntoContract(ContractDTO contract) {
        return modelMapper.map(contract, Contract.class);
    }
}
