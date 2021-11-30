package com.itrex.java.lab.repository.hibernatejdbc.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.hibernatejdbc.ContractRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JDBCContractRepositoryImplTest extends BaseRepositoryTest {

    @Qualifier("JDBCContractRepositoryImpl")
    @Autowired
    private ContractRepository repository;

    @Test
    void find_validData_shouldReturnContract() throws RepositoryException {
        //given
        int expectedContractId = 1;
        int expectedOwnerId = 1;
        String expectedDescription = "first contract";
        LocalDate expectedStartDate = LocalDate.parse("2022-01-01");
        LocalDate expectedEndDate = LocalDate.parse("2022-12-31");
        Integer expectedPrice = 28000;
        //when
        Contract actualContract = repository.find(expectedContractId).get();
        //then
        assertAll(
                () -> assertEquals(expectedContractId, actualContract.getId()),
                () -> assertEquals(expectedOwnerId, actualContract.getOwner().getId()),
                () -> assertEquals(expectedDescription, actualContract.getDescription()),
                () -> assertEquals(expectedStartDate, actualContract.getStartDate()),
                () -> assertEquals(expectedEndDate, actualContract.getEndDate()),
                () -> assertEquals(expectedPrice, actualContract.getStartPrice())
        );
    }

    @Test
    void findAll_validData_shouldReturnContractList() throws RepositoryException {
        //given
        int expectedContractListSize = 2;
        //when
        List<Contract> actualList = repository.findAll();
        //then
        assertEquals(expectedContractListSize, actualList.size());
    }

    @Test
    void findAllByUserId_shouldReturnContractList() throws RepositoryException {
        //given
        int userId = 1;
        int expectedContractListSize = 1;
        //when
        List<Contract> actualList = repository.findAllByUserId(userId);
        //then
        assertEquals(expectedContractListSize, actualList.size());

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
        User contractOwner = User.builder().id(1).build();
        Contract expectedContract = Contract.builder().id(1).owner(contractOwner).description("edited contract")
                .startDate(LocalDate.now().plusDays(2L)).endDate(LocalDate.now().plusDays(5L)).startPrice(50000).build();
        //when
        Contract actualContract = repository.update(expectedContract);
        //then
        assertContractEquals(expectedContract, actualContract);
    }

    @Test
    void add_validData_shouldReturnNewCreatedContract() throws RepositoryException {
        //given
        User contractOwner = User.builder().id(1).build();
        Contract expectedContract = Contract.builder().id(3).owner(contractOwner).description("edited contract")
                .startDate(LocalDate.now().plusDays(2L)).endDate(LocalDate.now().plusDays(5L)).startPrice(50000).build();
        //when
        Contract actualContract = repository.add(expectedContract).get();
        //then
        assertContractEquals(expectedContract, actualContract);
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
        User contractOwner = User.builder().id(1).build();
        Contract invalidContract = Contract.builder().id(3).owner(contractOwner)
                .startDate(LocalDate.now().plusDays(2L)).endDate(LocalDate.now().plusDays(5L)).startPrice(50000).build();
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithNullStartDate_shouldThrowsRepositoryException() {
        //given && when
        User contractOwner = User.builder().id(1).build();
        Contract invalidContract = Contract.builder().id(3).owner(contractOwner).description("edited contract")
                .endDate(LocalDate.now().plusDays(5L)).startPrice(50000).build();
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithNullEndDate_shouldThrowsRepositoryException() {
        //given && when
        User contractOwner = User.builder().id(1).build();
        Contract invalidContract = Contract.builder().id(3).owner(contractOwner).description("edited contract")
                .startDate(LocalDate.now().plusDays(2L)).startPrice(50000).build();
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithNullStartPrice_shouldThrowsRepositoryException() {
        //given && when
        User contractOwner = User.builder().id(1).build();
        Contract invalidContract = Contract.builder().id(3).owner(contractOwner).description("edited contract")
                .startDate(LocalDate.now().plusDays(2L)).endDate(LocalDate.now().plusDays(5L)).build();
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithEndDateBeforeStartDate_shouldThrowsRepositoryException() {
        //given && when
        User contractOwner = User.builder().id(1).build();
        Contract invalidContract = Contract.builder().id(3).owner(contractOwner).description("edited contract")
                .startDate(LocalDate.now().plusDays(6L)).endDate(LocalDate.now().plusDays(5L)).startPrice(50000).build();
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    private void assertContractEquals(Contract expectedContract, Contract actualContract) {
        assertAll(
                () -> assertEquals(expectedContract.getOwner().getId(), actualContract.getOwner().getId()),
                () -> assertEquals(expectedContract.getDescription(), actualContract.getDescription()),
                () -> assertEquals(expectedContract.getStartDate(), actualContract.getStartDate()),
                () -> assertEquals(expectedContract.getEndDate(), actualContract.getEndDate()),
                () -> assertEquals(expectedContract.getStartPrice(), actualContract.getStartPrice())
        );
    }
}