package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.ContractRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernateContractRepositoryImplTest extends BaseRepositoryTest {

    private final ContractRepository repository;

    public HibernateContractRepositoryImplTest() {
        super();
        this.repository = new HibernateContractRepositoryImpl(getSessionFactory().openSession());
    }

    @Test
    void find_validData_shouldReturnContract() throws RepositoryException {
        //given
        Contract expectedContract = new Contract(1, 1, "first contract",
                LocalDate.parse("2022-01-01"), LocalDate.parse("2022-12-31"), 28000);
        //when
        int contractId = 1;
        Optional<Contract> actualContract = repository.find(contractId);
        //then
        assertEquals(expectedContract, actualContract.get());
    }

    @Test
    void findAll_validData_shouldReturnContractList() throws RepositoryException {
        //given
        List<Contract> expectedList = new ArrayList<>();
        Contract expectedContract1 = new Contract(1, 1, "first contract",
                LocalDate.parse("2022-01-01"), LocalDate.parse("2022-12-31"), 28000);
        Contract expectedContract2 = new Contract(2, 2, "second contract",
                LocalDate.parse("2022-03-01"), LocalDate.parse("2022-09-30"), 30000);
        expectedList.add(expectedContract1);
        expectedList.add(expectedContract2);
        //when
        List<Contract> actualList = repository.findAll();
        //then
        assertEquals(expectedList, actualList);
    }

    @Test
    void delete_validData_shouldDeleteContract() throws RepositoryException {
        //when
        int contractId = 1;
        //then
        assertTrue(repository.delete(contractId));
    }

    @Test
    void delete_invalidData_shouldReturnFalse() throws RepositoryException {
        //when
        int contractId = 5;
        //then
        assertFalse(repository.delete(contractId));
    }

    @Test
    void update_validData_shouldUpdateExistedContract() throws RepositoryException {
        //given
        Contract expectedContract = new Contract(1, 1, "edited contract",
                LocalDate.now(), LocalDate.now(), 50000);
        //when
        Contract actualContract = repository.update(expectedContract);
        //then
        assertEquals(expectedContract, actualContract);
    }

    @Test
    void add_validData_shouldReturnNewCreatedContract() throws RepositoryException {
        //given
        Contract expectedContract = new Contract(3, 1, "new contract",
                LocalDate.now().plusDays(1L), LocalDate.now().plusDays(5L), 50000);
        //when
        Optional<Contract> actualContract = repository.add(expectedContract);
        //then
        assertEquals(expectedContract, actualContract.get());
    }

    @Test
    void add_null_shouldThrowsRepositoryException() {
        //given && when
        Contract nullContract = null;
        //then
        assertThrows(RepositoryException.class, () -> repository.add(nullContract));
    }

    @Test
    void add_contractWithNullDescription_shouldThrowsRepositoryException() {
        //given && when
        Contract invalidContract = new Contract(3, 1, null,
                LocalDate.now().plusDays(1L), LocalDate.now().plusDays(5L), 50000);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithNullStartDate_shouldThrowsRepositoryException() {
        //given && when
        Contract invalidContract = new Contract(3, 1, "null",
                null, LocalDate.now().plusDays(1L), 50000);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithNullEndDate_shouldThrowsRepositoryException() {
        //given && when
        Contract invalidContract = new Contract(3, 1, "null",
                LocalDate.now().plusDays(2L), null, 50000);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithNullStartPrice_shouldThrowsRepositoryException() {
        //given && when
        Contract invalidContract = new Contract(3, 1, "null",
                LocalDate.now().plusDays(1L), LocalDate.now().plusDays(5L), null);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithEndDateBeforeStartDate_shouldThrowsRepositoryException() {
        //given && when
        Contract invalidContract = new Contract(3, 1, "null",
                LocalDate.now().plusDays(10L), LocalDate.now().plusDays(5L), 50000);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }
}