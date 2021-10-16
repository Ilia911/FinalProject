package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.repository.ContractRepository;

import java.util.List;
import java.util.Optional;

public class ContractRepositoryImpl implements ContractRepository {
    @Override
    public Optional<Contract> find(int id) {
        return Optional.empty();
    }

    @Override
    public List<Contract> findAll() {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Contract update(Contract contract) {
        return null;
    }

    @Override
    public Optional<Contract> add(Contract contract) {
        return Optional.empty();
    }
}
