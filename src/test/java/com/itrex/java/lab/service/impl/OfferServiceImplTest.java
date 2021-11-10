package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.dto.ContractDTO;
import com.itrex.java.lab.entity.dto.OfferDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.OfferRepository;
import com.itrex.java.lab.service.OfferService;
import com.itrex.java.lab.service.TestServiceConfiguration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
class OfferServiceImplTest {

    @Autowired
    private OfferService service;
    @Autowired
    private OfferRepository repository;

    @Test
    void find_validData_shouldReturnOffer() throws RepositoryException, ServiceException {
        //given
        int expectedOfferId = 1;
        int expectedOwnerId = 3;
        int expectedContractId = 1;
        int expectedPrice = 27500;
        User owner = User.builder().id(expectedOwnerId).build();
        Contract contract = Contract.builder().id(expectedContractId).build();
        //when
        Mockito.when(repository.find(expectedOfferId)).thenReturn(Optional.of(Offer.builder()
                .id(expectedOfferId).offerOwner(owner).contract(contract).price(expectedPrice).build()));
        OfferDTO actualOffer = service.find(expectedOfferId).get();
        //then
        assertAll(
                () -> assertEquals(expectedOfferId, actualOffer.getId()),
                () -> assertEquals(expectedOwnerId, actualOffer.getOfferOwner().getId()),
                () -> assertEquals(expectedContractId, actualOffer.getContract().getId()),
                () -> assertEquals(expectedPrice, actualOffer.getPrice())
        );
    }

    @Test
    void findAll_validData_shouldReturnOfferList() throws RepositoryException, ServiceException {
        //given
        int expectedOfferListSize = 2;
        //when
        int contractId = 1;
        Mockito.when(repository.findAll(contractId)).thenReturn(Arrays.asList(new Offer(), new Offer()));
        List<OfferDTO> actualOfferList = service.findAll(contractId);
        //then
        assertEquals(expectedOfferListSize, actualOfferList.size());
    }

    @Test
    void delete_validData_shouldDeleteContract() throws RepositoryException, ServiceException {
        //given
        int offerId = 1;
        //when
        Mockito.when(repository.delete(offerId)).thenReturn(true);
        //then
        assertTrue(service.delete(offerId));
    }

    @Test
    void delete_invalidData_shouldDeleteContract() throws RepositoryException, ServiceException {
        //given
        int offerId = 5;
        //when
        Mockito.when(repository.delete(offerId)).thenReturn(false);
        //then
        assertFalse(service.delete(offerId));
    }

    @Test
    void update_validData_shouldUpdateOffer() throws RepositoryException, ServiceException {
        //given
        int expectedOfferId = 1;
        int expectedOfferOwnerId = 3;
        int expectedContractId = 1;
        int expectedPrice = 25000;

        User expectedOfferOwner = User.builder().id(expectedOfferOwnerId).build();
        Contract expectedContract = Contract.builder().id(expectedContractId).build();
        UserDTO expectedOfferOwnerDTO = UserDTO.builder().id(expectedOfferOwnerId).build();
        ContractDTO expectedContractDTO = ContractDTO.builder().id(expectedContractId).build();

        Offer expectedUpdatedOffer = Offer.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwner).contract(expectedContract).price(expectedPrice)
                .build();
        OfferDTO expectedUpdatedOfferDTO = OfferDTO.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwnerDTO).contract(expectedContractDTO).price(expectedPrice)
                .build();
        //when
        Mockito.when(repository.update(expectedUpdatedOffer)).thenReturn(expectedUpdatedOffer);
        OfferDTO actualUpdatedOffer = service.update(expectedUpdatedOffer);
        //then
        assertOfferEquals(expectedUpdatedOfferDTO, actualUpdatedOffer);
    }

    @Test
    void add_validData_shouldCreateOffer() throws RepositoryException, ServiceException {
        //given
        int expectedOfferId = 3;
        int expectedOfferOwnerId = 4;
        int expectedContractId = 2;
        int expectedPrice = 27000;

        User expectedOfferOwner = User.builder().id(expectedOfferOwnerId).build();
        Contract expectedContract = Contract.builder().id(expectedContractId).build();
        UserDTO expectedOfferOwnerDTO = UserDTO.builder().id(expectedOfferOwnerId).build();
        ContractDTO expectedContractDTO = ContractDTO.builder().id(expectedContractId).build();

        Offer expectedNewOffer = Offer.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwner).contract(expectedContract).price(expectedPrice)
                .build();
        OfferDTO expectedNewOfferDTO = OfferDTO.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwnerDTO).contract(expectedContractDTO).price(expectedPrice)
                .build();
        //when
        Mockito.when(repository.add(expectedNewOffer)).thenReturn(Optional.of(expectedNewOffer));
        Optional<OfferDTO> actualNewOffer = service.add(expectedNewOffer);
        //then
        assertOfferEquals(expectedNewOfferDTO, actualNewOffer.get());
    }

    @Test
    void add_null_shouldThrowServiceException() throws RepositoryException {
        //given
        Offer offer = null;
        //when
        Mockito.when(repository.add(offer)).thenThrow(new RepositoryException());
        //then
        assertThrows(ServiceException.class, () -> service.add(offer));
    }

    @Test
    void add_offerWithNullPrice_shouldThrowServiceException() throws RepositoryException {
        //given
        int expectedOfferId = 3;
        int expectedOfferOwnerId = 4;
        int expectedContractId = 2;
        Integer expectedPrice = null;

        User expectedOfferOwner = User.builder().id(expectedOfferOwnerId).build();
        Contract expectedContract = Contract.builder().id(expectedContractId).build();

        Offer offer = Offer.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwner).contract(expectedContract).price(expectedPrice)
                .build();
        //when
        Mockito.when(repository.add(offer)).thenThrow(new RepositoryException());
        //then
        assertThrows(ServiceException.class, () -> service.add(offer));
    }

    @Test
    void add_offerWithZeroPrice_shouldThrowServiceException() throws RepositoryException {
        //given
        int expectedOfferId = 3;
        int expectedOfferOwnerId = 4;
        int expectedContractId = 2;
        Integer expectedPrice = 0;

        User expectedOfferOwner = User.builder().id(expectedOfferOwnerId).build();
        Contract expectedContract = Contract.builder().id(expectedContractId).build();

        Offer offer = Offer.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwner).contract(expectedContract).price(expectedPrice)
                .build();
        //when
        Mockito.when(repository.add(offer)).thenThrow(new RepositoryException());
        //then
        assertThrows(ServiceException.class, () -> service.add(offer));
    }

    private void assertOfferEquals(OfferDTO expected, OfferDTO actualOffer) {
        assertEquals(expected.getId(), actualOffer.getId());
        assertEquals(expected.getOfferOwner().getId(), actualOffer.getOfferOwner().getId());
        assertEquals(expected.getContract().getId(), actualOffer.getContract().getId());
        assertEquals(expected.getPrice(), actualOffer.getPrice());
    }
}