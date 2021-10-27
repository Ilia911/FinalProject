package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.OfferRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernateOfferRepositoryImplTest extends BaseRepositoryTest {

    private final OfferRepository repository;

    protected final Role contractorRole;
    protected final Role customerRole;
    protected final User firstCustomer;
    protected final User secondCustomer;
    protected final User firstContractor;
    protected final User secondContractor;
    protected final Contract firstContract;
    protected final Contract secondContract;
    protected final Offer firstOffer;
    protected final Offer secondOffer;

    {
        contractorRole = new Role(3, "contractor");
        customerRole = new Role(2, "customer");

        firstCustomer = new User(1, "Customer", "password", customerRole, "castomer@gmail.com", new ArrayList<>());
        secondCustomer = new User(2, "SecondCustomer", "password", customerRole, "secondCastomer@gmail.com", new ArrayList<>());

        List<Certificate> firstContractorCertificates = new ArrayList<>();
        firstContractorCertificates.add(new Certificate(1, "Filling window and door openings"));
        firstContractorCertificates.add(new Certificate(6, "Execution of work on the arrangement of foundations, foundations of buildings and structures"));
        firstContractorCertificates.add(new Certificate(7, "Performing work on the installation of thermal insulation of the enclosing structures of buildings and structures"));
        firstContractor = new User(3, "Contractor", "password", contractorRole, "contractor@gmail.com", firstContractorCertificates);

        List<Certificate> secondContractorCertificates = new ArrayList<>();
        secondContractorCertificates.add(new Certificate(4, "Execution of works on the arrangement of road surfaces of pedestrian zones from sidewalk slabs"));
        secondContractorCertificates.add(new Certificate(5, "Execution of works on the construction of insulating coatings"));
        secondContractor = new User(4, "SecondContractor", "password", contractorRole, "SecondContractor@gmail.com", secondContractorCertificates);

        firstContract = new Contract(1, firstCustomer, "first contract", LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31), 28000);
        secondContract = new Contract(2, secondCustomer, "second contract", LocalDate.of(2022, 3, 1), LocalDate.of(2022, 9, 3), 30000);

        firstOffer = new Offer(1, firstContractor, firstContract, 27500);
        secondOffer = new Offer(2, secondContractor, firstContract, 26000);
    }

    public HibernateOfferRepositoryImplTest() {
        super();
        this.repository = new HibernateOfferRepositoryImpl(getSessionFactory().openSession());
    }

    @Test
    void find_validData_shouldReturnOffer() throws RepositoryException {
        //given
        Offer expected = firstOffer;
        //when
        int offerId = 1;
        Optional<Offer> actualOffer = repository.find(offerId);
        //then
        assertOfferEquals(expected, actualOffer.get());
    }

    @Test
    void find_notExistsOfferId_shouldReturnOptionalEmpty() throws RepositoryException {
        //given && when
        int offerId = 11;
        //then
        assertTrue(repository.find(offerId).isEmpty());
    }

    @Test
    void findAll_validData_shouldReturnOfferList() throws RepositoryException {
        //given
        List<Offer> expectedOfferList = new ArrayList<>();
        expectedOfferList.add(firstOffer);
        expectedOfferList.add(secondOffer);
        //when
        int contractId = 1;
        List<Offer> actualOfferList = repository.findAll(contractId);
        //then
        for (int i = 0; i < expectedOfferList.size(); i++) {
            assertOfferEquals(expectedOfferList.get(i), actualOfferList.get(i));
        }
    }

    @Test
    void delete_validData_shouldDeleteContract() throws RepositoryException {
        //when
        int offerId = 1;
        //then
        assertTrue(repository.delete(offerId));
    }

    @Test
    void delete_invalidData_shouldDeleteContract() throws RepositoryException {
        //when
        int offerId = 5;
        //then
        assertFalse(repository.delete(offerId));
    }

    @Test
    void update_validData_shouldUpdateOffer() throws RepositoryException {
        //given
        Offer expectedUpdatedOffer = new Offer(1, firstCustomer, firstContract, 24000);
        //when
        Offer actualUpdatedOffer = repository.update(expectedUpdatedOffer);
        //then
        assertEquals(expectedUpdatedOffer, actualUpdatedOffer);
    }

    @Test
    void add_validData_shouldCreateOffer() throws RepositoryException {
        //given
        Offer expectedNewOffer = new Offer(3, secondCustomer, secondContract, 27000);
        //when
        Optional<Offer> actualNewOffer = repository.add(expectedNewOffer);
        //then
        assertEquals(expectedNewOffer, actualNewOffer.get());
    }

    @Test
    void add_null_shouldThrowRepositoryException() {
        //given && when
        Offer offer = null;
        //then
        assertThrows(RepositoryException.class, () -> repository.add(offer));
    }

    @Test
    void add_offerWithNullPrice_shouldThrowRepositoryException() {
        //given && when
        Offer offer = new Offer(3, secondCustomer, secondContract, null);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(offer));
    }

    @Test
    void add_offerWithZeroPrice_shouldThrowRepositoryException() {
        //given && when
        Offer offer = new Offer(3, secondCustomer, secondContract, 0);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(offer));
    }

    private void assertOfferEquals(Offer expected, Offer actualOffer) {
        assertEquals(expected.getId(), actualOffer.getId());
        assertEquals(expected.getOfferOwner().getId(), actualOffer.getOfferOwner().getId());
        assertEquals(expected.getContract().getId(), actualOffer.getContract().getId());
        assertEquals(expected.getPrice(), actualOffer.getPrice());
    }
}