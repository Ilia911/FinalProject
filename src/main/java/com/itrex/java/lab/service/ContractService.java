package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.dto.ContractDTO;
import com.itrex.java.lab.exeption.ServiceException;
import java.util.List;
import java.util.Optional;

public interface ContractService {

    Optional<ContractDTO> find(int id) throws ServiceException;

    List<ContractDTO> findAll() throws ServiceException;

    boolean delete(int id) throws ServiceException;

    ContractDTO update(Contract contract) throws ServiceException;

    Optional<ContractDTO> add(Contract contract) throws ServiceException;
}
