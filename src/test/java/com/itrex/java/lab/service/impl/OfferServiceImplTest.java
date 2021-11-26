package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.dto.ContractDTO;
import com.itrex.java.lab.entity.dto.OfferDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
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
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void find_validData_shouldReturnOffer() {
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
        when(repository.findById(expectedOfferId)).thenReturn(Optional.of(offer));
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
    void findAll_validData_shouldReturnOfferList() {
        //given
        int expectedOfferListSize = 2;
        Offer offer = Offer.builder().build();
        OfferDTO offerDTO = OfferDTO.builder().build();
        int contractId = 1;
        //when
        when(modelMapper.map(offer, OfferDTO.class)).thenReturn(offerDTO);
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
        when(repository.findById(expectedOfferId)).thenReturn(Optional.of(originalOffer));
        when(repository.findById(expectedOfferId)).thenReturn(Optional.of(offerAfterUpdate));
        when(modelMapper.map(offerAfterUpdate, OfferDTO.class)).thenReturn(offerDTO);
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
        when(repository.save(offer)).thenReturn(offer);
        when(modelMapper.map(offer, OfferDTO.class)).thenReturn(offerDTO);
        Optional<OfferDTO> actualNewOffer = service.add(offerDTO);
        //then
        assertOfferEquals(offerDTO, actualNewOffer.get());
    }

    @Test
    void add_null_shouldThrowServiceException() {
        //given
        Offer offer = null;
        OfferDTO offerDTO = null;
        //when
        when(repository.save(offer)).thenThrow(new IllegalArgumentException());
        //then
        assertThrows(IllegalArgumentException.class, () -> service.add(offerDTO));
    }

    private void assertOfferEquals(OfferDTO expected, OfferDTO actualOffer) {
        assertEquals(expected.getId(), actualOffer.getId());
        assertEquals(expected.getOfferOwner().getId(), actualOffer.getOfferOwner().getId());
        assertEquals(expected.getContract().getId(), actualOffer.getContract().getId());
        assertEquals(expected.getPrice(), actualOffer.getPrice());
    }
}