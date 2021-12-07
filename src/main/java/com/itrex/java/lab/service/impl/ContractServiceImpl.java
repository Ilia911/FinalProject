package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.dto.ContractDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.data.ContractRepository;
import com.itrex.java.lab.repository.data.OfferRepository;
import com.itrex.java.lab.service.ContractService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final OfferRepository offerRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<ContractDTO> find(int id) {
        ContractDTO contractDTO = null;

        Optional<Contract> contract = contractRepository.findById(id);
        if (contract.isPresent()) {
            contractDTO = ContractDTO.convertFromContract(contract.get());
        }
        return Optional.ofNullable(contractDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractDTO> findAll() {
        List<ContractDTO> contractDTOs = new ArrayList<>();

        List<Contract> contracts = contractRepository.findAll();
        if (contracts.size() > 0) {
            contractDTOs = contracts.stream().map(ContractDTO::convertFromContract).collect(Collectors.toList());
        }
        return contractDTOs;
    }

    @Override
    @Transactional
    public boolean delete(int id) {

        List<Offer> offers = offerRepository.findAllByContractId(id);
        for (Offer offer : offers) {
            offer.removeContract();
            offerRepository.flush();
        }
        contractRepository.deleteById(id);
        return contractRepository.findById(id).isEmpty();
    }

    @Override
    @Transactional
    public ContractDTO update(ContractDTO contractDTO) throws ServiceException {

        Optional<Contract> optionalContract = contractRepository.findById(contractDTO.getId());
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
            contractRepository.flush();
            return ContractDTO.convertFromContract(contractRepository.findById(contractDTO.getId()).get());
        } else {
            throw new ServiceException(String.format("Contract with id = %d does not exist", contractDTO.getId()));
        }
    }

    @Override
    @Transactional
    public Optional<ContractDTO> add(ContractDTO contractDTO) {
        ContractDTO newContractDTO;
        Contract contract = ContractDTO.convertToContract(contractDTO);
        Contract newContract = contractRepository.save(contract);
        newContractDTO = ContractDTO.convertFromContract(newContract);
        return Optional.ofNullable(newContractDTO);
    }
}
