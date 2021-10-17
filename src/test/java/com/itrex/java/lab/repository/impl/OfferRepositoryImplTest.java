package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.OfferRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class OfferRepositoryImplTest extends BaseRepositoryTest {

    private final OfferRepository repository;

    public OfferRepositoryImplTest() {
        super();
        repository = new OfferRepositoryImpl(getConnectionPool());
    }

    @Test
    void find_validDate_shouldReturnOffer() {
        Offer expected = new Offer(1, 3, 1, 27500);

        Optional<Offer> actualOffer = repository.find(1);

        assertEquals(expected, actualOffer.get());
    }

    @Test
    void findAll_validData_shouldReturnOfferList() {
        List<Offer> expectedOfferList = new ArrayList<>();
        Offer expectedOffer1 = new Offer(1, 3, 1, 27500);
        Offer expectedOffer2 = new Offer(2, 4, 1, 26000);
        expectedOfferList.add(expectedOffer1);
        expectedOfferList.add(expectedOffer2);

        List<Offer> actualOfferList = repository.findAll(1);

        assertEquals(expectedOfferList, actualOfferList);
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
    void update_validData_shouldUpdateOffer() {
        Offer expectedUpdatedOffer = new Offer(1, 3, 1, 24000);

        Offer actualUpdatedOffer = repository.update(expectedUpdatedOffer);

        assertEquals(expectedUpdatedOffer, actualUpdatedOffer);
    }

    @Test
    void add_validData_shouldCreateOffer() {
        Offer expectedNewOffer = new Offer(3, 4, 2, 27000);

        Optional<Offer> actualNewOffer = repository.add(expectedNewOffer);

        assertEquals(expectedNewOffer, actualNewOffer.get());
    }
}