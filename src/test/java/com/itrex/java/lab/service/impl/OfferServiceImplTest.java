package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.dto.OfferDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.data.OfferRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @InjectMocks
    private OfferServiceImpl service;
    @Mock
    private OfferRepository repository;

    @Test
    void find_validData_shouldReturnOffer() {
        //given
        int expectedOfferId = 1;
        int ownerId = 3;
        int contractId = 1;
        int expectedPrice = 27500;
        User owner = User.builder().id(ownerId).build();
        Contract contract = Contract.builder().id(contractId).build();
        Offer offer = Offer.builder().id(expectedOfferId).offerOwner(owner).contract(contract).price(expectedPrice).build();
        //when
        when(repository.findById(expectedOfferId)).thenReturn(Optional.of(offer));
        OfferDTO actualOffer = service.find(expectedOfferId).get();
        //then
        assertAll(
                () -> assertEquals(expectedOfferId, actualOffer.getId()),
                () -> assertEquals(ownerId, actualOffer.getOfferOwnerId()),
                () -> assertEquals(contractId, actualOffer.getContractId()),
                () -> assertEquals(expectedPrice, actualOffer.getPrice())
        );
    }

    @Test
    void findAll_validData_shouldReturnOfferList() {
        //given
        int expectedOfferListSize = 2;
        Offer offer = Offer.builder().id(1).offerOwner(User.builder().id(3).build()).contract(Contract.builder().id(1).build()).build();
        int contractId = 1;
        //when
        when(repository.findAllByContractId(contractId)).thenReturn(Arrays.asList(offer, offer));
        List<OfferDTO> actualOfferList = service.findAll(contractId);
        //then
        assertEquals(expectedOfferListSize, actualOfferList.size());
    }

    @Test
    void delete_validData_shouldDeleteContract() {
        //given
        int offerId = 1;
        //when
        when(repository.findById(offerId)).thenReturn(Optional.empty());
        //then
        assertTrue(service.delete(offerId));
    }

    @Test
    void update_validData_shouldUpdateOffer() throws ServiceException {
        //given
        int expectedOfferId = 1;
        int offerOwnerID = 3;
        int contractId = 1;
        int originalPrice = 26000;
        int expectedPrice = 25000;
        User expectedOfferOwner = User.builder().id(offerOwnerID).build();
        Contract expectedContract = Contract.builder().id(contractId).build();
        Offer originalOffer = Offer.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwner).contract(expectedContract).price(originalPrice)
                .build();
        Offer offerAfterUpdate = Offer.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwner).contract(expectedContract).price(expectedPrice)
                .build();
        OfferDTO offerDTO = OfferDTO.builder()
                .id(expectedOfferId).offerOwnerId(offerOwnerID).contractId(contractId).price(expectedPrice)
                .build();
        //when
        when(repository.findById(expectedOfferId)).thenReturn(Optional.of(originalOffer));
        when(repository.findById(expectedOfferId)).thenReturn(Optional.of(offerAfterUpdate));
        OfferDTO actualUpdatedOffer = service.update(offerDTO);
        //then
        assertOfferEquals(offerDTO, actualUpdatedOffer);
    }

    @Test
    void add_validData_shouldCreateOffer() {
        //given
        int id = 3;
        int ownerId = 4;
        int contractId = 2;
        int price = 27000;
        User owner = User.builder().id(ownerId).build();
        Contract contract = Contract.builder().id(contractId).build();
        Offer offer = Offer.builder()
                .id(id).offerOwner(owner).contract(contract).price(price)
                .build();
        OfferDTO offerDTO = OfferDTO.builder()
                .id(id).offerOwnerId(ownerId).contractId(contractId).price(price)
                .build();
        //when
        when(repository.save(offer)).thenReturn(offer);
        Optional<OfferDTO> actualNewOffer = service.add(offerDTO);
        //then
        assertOfferEquals(offerDTO, actualNewOffer.get());
    }

    private void assertOfferEquals(OfferDTO expected, OfferDTO actualOffer) {
        assertEquals(expected.getId(), actualOffer.getId());
        assertEquals(expected.getOfferOwnerId(), actualOffer.getOfferOwnerId());
        assertEquals(expected.getContractId(), actualOffer.getContractId());
        assertEquals(expected.getPrice(), actualOffer.getPrice());
    }
}