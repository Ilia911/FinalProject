package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.exeption.RepositoryException;
import java.util.List;
import java.util.Optional;

public interface ContractRepository {

    Optional<Contract> find(int id) throws RepositoryException;

    List<Contract> findAll() throws RepositoryException;

    boolean delete(int id) throws RepositoryException;

    Contract update(Contract contract) throws RepositoryException;

    Optional<Contract> add(Contract contract) throws RepositoryException;
}

