package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.OfferRepository;
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

    public HibernateOfferRepositoryImplTest() {
        super();
        this.repository = new HibernateOfferRepositoryImpl(getSessionFactory().openSession());
    }

    @Test
    void find_validData_shouldReturnOffer() throws RepositoryException {
        //given
        Offer expected = new Offer(1, 3, 1, 27500);
        //when
        int offerId = 1;
        Optional<Offer> actualOffer = repository.find(offerId);
        //then
        assertEquals(expected, actualOffer.get());
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
        Offer expectedOffer1 = new Offer(1, 3, 1, 27500);
        Offer expectedOffer2 = new Offer(2, 4, 1, 26000);
        expectedOfferList.add(expectedOffer1);
        expectedOfferList.add(expectedOffer2);
        //when
        int offerId = 1;
        List<Offer> actualOfferList = repository.findAll(offerId);
        //then
        assertEquals(expectedOfferList, actualOfferList);
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
        Offer expectedUpdatedOffer = new Offer(1, 3, 1, 24000);
        //when
        Offer actualUpdatedOffer = repository.update(expectedUpdatedOffer);
        //then
        assertEquals(expectedUpdatedOffer, actualUpdatedOffer);
    }

    @Test
    void add_validData_shouldCreateOffer() throws RepositoryException {
        //given
        Offer expectedNewOffer = new Offer(3, 4, 2, 27000);
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
        Offer offer = new Offer(3, 4, 2, null);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(offer));
    }

    @Test
    void add_offerWithZeroPrice_shouldThrowRepositoryException() {
        //given && when
        Offer offer = new Offer(3, 4, 2, 0);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(offer));
    }
}