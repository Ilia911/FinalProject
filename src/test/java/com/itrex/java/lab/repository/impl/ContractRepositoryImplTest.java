package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.ContractRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContractRepositoryImplTest extends BaseRepositoryTest {

    private final ContractRepository repository;

    ContractRepositoryImplTest() {
        super();
        repository = new ContractRepositoryImpl(getConnectionPool());
    }

    @Test
    void find_validData_shouldReturnContract() {
        Contract expectedContract = new Contract(1, 1, "first contract",
                LocalDate.parse("2022-01-01"), LocalDate.parse("2022-12-31"), 28000);

        Optional<Contract> actualContract = repository.find(1);

        assertEquals(expectedContract, actualContract.get());
    }

    @Test
    void findAll_validData_shouldReturnContractList() {
        List<Contract> expectedList = new ArrayList<>();
        Contract expectedContract1 = new Contract(1, 1, "first contract",
                LocalDate.parse("2022-01-01"), LocalDate.parse("2022-12-31"), 28000);
        Contract expectedContract2 = new Contract(2, 2, "second contract",
                LocalDate.parse("2022-03-01"), LocalDate.parse("2022-09-30"), 30000);
        expectedList.add(expectedContract1);
        expectedList.add(expectedContract2);

        List<Contract> actualList = repository.findAll();

        assertEquals(expectedList, actualList);
    }

    @Test
    void delete_validData_shouldDeleteContract() {
        assertTrue(repository.delete(1));
    }

    @Test
    void delete_invalidData_shouldDeleteContract() {
        assertFalse(repository.delete(5));
    }

    @Test
    void update_validData_shouldUpdateExistedContract() {
        Contract expectedContract = new Contract(1, 1, "edited contract",
                LocalDate.parse("2022-06-01"), LocalDate.parse("2022-08-30"), 50000);

        Contract actualContract = repository.update(expectedContract);

        assertEquals(expectedContract, actualContract);
    }

    @Test
    void add_validData_shouldReturnNewCreatedContract() {
        Contract expectedContract = new Contract(3, 1, "new contract",
                LocalDate.parse("2022-06-01"), LocalDate.parse("2022-08-30"), 50000);

        Optional<Contract> actualContract = repository.add(expectedContract);

        assertEquals(expectedContract, actualContract.get());
    }
}