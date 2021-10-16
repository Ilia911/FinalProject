package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Contract;

import java.util.List;
import java.util.Optional;

public interface ContractRepository {

    Optional<Contract> find(int id);

    List<Contract> findAll();

    boolean delete(int id);

    Contract update(Contract contract);

    Optional<Contract> add(Contract contract);
}

