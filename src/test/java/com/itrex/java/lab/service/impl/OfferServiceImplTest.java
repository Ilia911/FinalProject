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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @InjectMocks
    private OfferServiceImpl service;
    @Mock
    private OfferRepository repository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void find_validData_shouldReturnOffer() throws RepositoryException, ServiceException {
        //given
        int expectedOfferId = 1;
        int expectedOwnerId = 3;
        int expectedContractId = 1;
        int expectedPrice = 27500;
        User owner = User.builder().id(expectedOwnerId).build();
        Contract contract = Contract.builder().id(expectedContractId).build();
        Offer offer = Offer.builder().id(expectedOfferId).offerOwner(owner).contract(contract).price(expectedPrice).build();
        UserDTO ownerDTO = UserDTO.builder().id(expectedOwnerId).build();
        ContractDTO contractDTO = ContractDTO.builder().id(expectedContractId).build();
        OfferDTO offerDTO = OfferDTO.builder().id(expectedOfferId).offerOwner(ownerDTO).contract(contractDTO).price(expectedPrice).build();
        //when
        when(repository.find(expectedOfferId)).thenReturn(Optional.of(offer));
        when(modelMapper.map(offer, OfferDTO.class)).thenReturn(offerDTO);
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
        Offer offer = Offer.builder().build();
        OfferDTO offerDTO = OfferDTO.builder().build();
        int contractId = 1;
        //when
        when(modelMapper.map(offer, OfferDTO.class)).thenReturn(offerDTO);
        when(repository.findAll(contractId)).thenReturn(Arrays.asList(offer, offer));
        List<OfferDTO> actualOfferList = service.findAll(contractId);
        //then
        assertEquals(expectedOfferListSize, actualOfferList.size());
    }

    @Test
    void delete_validData_shouldDeleteContract() throws RepositoryException, ServiceException {
        //given
        int offerId = 1;
        //when
        when(repository.delete(offerId)).thenReturn(true);
        //then
        assertTrue(service.delete(offerId));
    }

    @Test
    void delete_invalidData_shouldDeleteContract() throws RepositoryException, ServiceException {
        //given
        int offerId = 5;
        //when
        when(repository.delete(offerId)).thenReturn(false);
        //then
        assertFalse(service.delete(offerId));
    }

    @Test
    void update_validData_shouldUpdateOffer() throws RepositoryException, ServiceException {
        //given
        int expectedOfferId = 1;
        int expectedOfferOwnerId = 3;
        int expectedContractId = 1;
        int originalPrice = 26000;
        int expectedPrice = 25000;
        User expectedOfferOwner = User.builder().id(expectedOfferOwnerId).build();
        Contract expectedContract = Contract.builder().id(expectedContractId).build();
        UserDTO expectedOfferOwnerDTO = UserDTO.builder().id(expectedOfferOwnerId).build();
        ContractDTO expectedContractDTO = ContractDTO.builder().id(expectedContractId).build();

        Offer originalOffer = Offer.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwner).contract(expectedContract).price(originalPrice)
                .build();
        Offer offerAfterUpdate = Offer.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwner).contract(expectedContract).price(expectedPrice)
                .build();
        OfferDTO offerDTO = OfferDTO.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwnerDTO).contract(expectedContractDTO).price(expectedPrice)
                .build();
        //when
        when(repository.find(expectedOfferId)).thenReturn(Optional.of(originalOffer));
        when(repository.update(offerAfterUpdate)).thenReturn(offerAfterUpdate);
        when(modelMapper.map(offerAfterUpdate, OfferDTO.class)).thenReturn(offerDTO);
        OfferDTO actualUpdatedOffer = service.update(offerDTO);
        //then
        assertOfferEquals(offerDTO, actualUpdatedOffer);
    }

    @Test
    void add_validData_shouldCreateOffer() throws RepositoryException, ServiceException {
        //given
        int id = 3;
        int ownerId = 4;
        int contractId = 2;
        int price = 27000;
        User owner = User.builder().id(ownerId).build();
        Contract contract = Contract.builder().id(contractId).build();
        UserDTO userDTO = UserDTO.builder().id(ownerId).build();
        ContractDTO contractDTO = ContractDTO.builder().id(contractId).build();
        Offer offer = Offer.builder()
                .id(id).offerOwner(owner).contract(contract).price(price)
                .build();
        OfferDTO offerDTO = OfferDTO.builder()
                .id(id).offerOwner(userDTO).contract(contractDTO).price(price)
                .build();
        //when
        when(modelMapper.map(offerDTO, Offer.class)).thenReturn(offer);
        when(repository.add(offer)).thenReturn(Optional.of(offer));
        when(modelMapper.map(offer, OfferDTO.class)).thenReturn(offerDTO);
        Optional<OfferDTO> actualNewOffer = service.add(offerDTO);
        //then
        assertOfferEquals(offerDTO, actualNewOffer.get());
    }

    @Test
    void add_null_shouldThrowServiceException() throws RepositoryException {
        //given
        Offer offer = null;
        OfferDTO offerDTO = null;
        //when
        when(repository.add(offer)).thenThrow(new RepositoryException());
        //then
        assertThrows(ServiceException.class, () -> service.add(offerDTO));
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
        UserDTO expectedOfferOwnerDTO = UserDTO.builder().id(expectedOfferOwnerId).build();
        ContractDTO expectedContractDTO = ContractDTO.builder().id(expectedContractId).build();
        Offer offer = Offer.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwner).contract(expectedContract).price(expectedPrice)
                .build();
        OfferDTO offerDTO = OfferDTO.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwnerDTO).contract(expectedContractDTO).price(expectedPrice)
                .build();
        //when
        when(modelMapper.map(offerDTO, Offer.class)).thenReturn(offer);
        when(repository.add(offer)).thenThrow(new RepositoryException());
        //then
        assertThrows(ServiceException.class, () -> service.add(offerDTO));
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
        UserDTO expectedOfferOwnerDTO = UserDTO.builder().id(expectedOfferOwnerId).build();
        ContractDTO expectedContractDTO = ContractDTO.builder().id(expectedContractId).build();
        Offer offer = Offer.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwner).contract(expectedContract).price(expectedPrice)
                .build();
        OfferDTO offerDTO = OfferDTO.builder()
                .id(expectedOfferId).offerOwner(expectedOfferOwnerDTO).contract(expectedContractDTO).price(expectedPrice)
                .build();
        //when
        when(modelMapper.map(offerDTO, Offer.class)).thenReturn(offer);
        when(repository.add(offer)).thenThrow(new RepositoryException());
        //then
        assertThrows(ServiceException.class, () -> service.add(offerDTO));
    }

    private void assertOfferEquals(OfferDTO expected, OfferDTO actualOffer) {
        assertEquals(expected.getId(), actualOffer.getId());
        assertEquals(expected.getOfferOwner().getId(), actualOffer.getOfferOwner().getId());
        assertEquals(expected.getContract().getId(), actualOffer.getContract().getId());
        assertEquals(expected.getPrice(), actualOffer.getPrice());
    }
}