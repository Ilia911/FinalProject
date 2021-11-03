package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.ContractRepository;
import com.itrex.java.lab.repository.TestRepositoryConfiguration;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
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
        assertEquals(expectedContractId, actualContract.getId());
        assertEquals(expectedOwnerId, actualContract.getOwner().getId());
        assertEquals(expectedDescription, actualContract.getDescription());
        assertEquals(expectedStartDate, actualContract.getStartDate());
        assertEquals(expectedEndDate, actualContract.getEndDate());
        assertEquals(expectedPrice, actualContract.getStartPrice());
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
        User contractOwner = new User();
        contractOwner.setId(1);

        Contract expectedContract = new Contract(1, contractOwner, "edited contract",
                LocalDate.now().plusDays(2L), LocalDate.now().plusDays(5L), 50000);
        //when
        Contract actualContract = repository.update(expectedContract);
        //then
        assertContractEquals(expectedContract, actualContract);
    }

    @Test
    void add_validData_shouldReturnNewCreatedContract() throws RepositoryException {
        //given
        User contractOwner = new User();
        contractOwner.setId(1);

        Contract expectedContract = new Contract(3, contractOwner, "new contract",
                LocalDate.now().plusDays(1L), LocalDate.now().plusDays(5L), 50000);
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
        User contractOwner = new User();
        contractOwner.setId(1);

        Contract invalidContract = new Contract(3, contractOwner, null,
                LocalDate.now().plusDays(1L), LocalDate.now().plusDays(5L), 50000);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithNullStartDate_shouldThrowsRepositoryException() {
        //given && when
        User contractOwner = new User();
        contractOwner.setId(1);

        Contract invalidContract = new Contract(3, contractOwner, "null",
                null, LocalDate.now().plusDays(1L), 50000);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithNullEndDate_shouldThrowsRepositoryException() {
        //given && when
        User contractOwner = new User();
        contractOwner.setId(1);

        Contract invalidContract = new Contract(3, contractOwner, "null",
                LocalDate.now().plusDays(2L), null, 50000);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithNullStartPrice_shouldThrowsRepositoryException() {
        //given && when
        User contractOwner = new User();
        contractOwner.setId(1);

        Contract invalidContract = new Contract(3, contractOwner, "null",
                LocalDate.now().plusDays(1L), LocalDate.now().plusDays(5L), null);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    @Test
    void add_contractWithEndDateBeforeStartDate_shouldThrowsRepositoryException() {
        //given && when
        User contractOwner = new User();
        contractOwner.setId(1);

        Contract invalidContract = new Contract(3, contractOwner, "null",
                LocalDate.now().plusDays(10L), LocalDate.now().plusDays(5L), 50000);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(invalidContract));
    }

    private void assertContractEquals(Contract expectedContract, Contract actualContract) {
        assertEquals(expectedContract.getId(), actualContract.getId());
        assertEquals(expectedContract.getOwner().getId(), actualContract.getOwner().getId());
        assertEquals(expectedContract.getDescription(), actualContract.getDescription());
        assertEquals(expectedContract.getStartDate(), actualContract.getStartDate());
        assertEquals(expectedContract.getEndDate(), actualContract.getEndDate());
        assertEquals(expectedContract.getStartPrice(), actualContract.getStartPrice());
    }
}