package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.dto.ContractDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.data.ContractRepository;
import com.itrex.java.lab.repository.data.OfferRepository;
import java.time.LocalDate;
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
class ContractServiceImplTest {

    @InjectMocks
    private ContractServiceImpl service;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private OfferRepository offerRepository;

    @Test
    void find_validData_shouldReturnContract() {
        //given
        int expectedContractId = 1;
        int ownerId = 1;
        User owner = User.builder().id(ownerId).build();
        String expectedDescription = "first contract";
        LocalDate expectedStartDate = LocalDate.parse("2022-01-01");
        LocalDate expectedEndDate = LocalDate.parse("2022-12-31");
        Integer expectedPrice = 28000;
        //when
        Contract contract = Contract.builder()
                .id(expectedContractId).owner(owner).description(expectedDescription).startDate(expectedStartDate)
                .endDate(expectedEndDate).startPrice(expectedPrice)
                .build();
        when(contractRepository.findById(expectedContractId)).thenReturn(Optional.of(contract));
        ContractDTO actualContract = service.find(expectedContractId).get();
        //then
        assertAll(
                () -> assertEquals(expectedContractId, actualContract.getId()),
                () -> assertEquals(ownerId, actualContract.getOwnerId()),
                () -> assertEquals(expectedDescription, actualContract.getDescription()),
                () -> assertEquals(expectedStartDate, actualContract.getStartDate()),
                () -> assertEquals(expectedEndDate, actualContract.getEndDate()),
                () -> assertEquals(expectedPrice, actualContract.getStartPrice())
        );
    }

    @Test
    void findAll_validData_shouldReturnContractList() {
        //given
        int expectedContractListSize = 2;
        Contract contract = Contract.builder().id(1).owner(User.builder().id(1).build()).build();
        //when
        when(contractRepository.findAll()).thenReturn(Arrays.asList(contract, contract));
        List<ContractDTO> actualList = service.findAll();
        //then
        assertEquals(expectedContractListSize, actualList.size());
    }

    @Test
    void delete_validData_shouldDeleteContract() {
        //given
        int contractId = 1;
        int offerId = 1;
        Offer offer = Offer.builder().id(offerId).contract(Contract.builder().id(contractId).build()).build();
        //when
        when(offerRepository.findAllByContractId(contractId)).thenReturn(List.of(offer));
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());
        //then
        assertTrue(service.delete(contractId));
    }

    @Test
    void update_validData_shouldUpdateExistedContract() throws ServiceException {
        //given
        int contractId = 1;
        int ownerId = 1;
        User contractOwner = User.builder().id(1).build();
        Contract originalContract = Contract.builder()
                .id(contractId).owner(contractOwner).description("original name").startDate(LocalDate.now().plusDays(1L))
                .endDate(LocalDate.now().plusDays(2L)).startPrice(40000)
                .build();
        Contract expectedContract = Contract.builder()
                .id(contractId).owner(contractOwner).description("edited contract").startDate(LocalDate.now().plusDays(2L))
                .endDate(LocalDate.now().plusDays(5L)).startPrice(50000)
                .build();
        ContractDTO expectedContractDTO = ContractDTO.builder()
                .id(contractId).ownerId(ownerId).description("edited contract").startDate(LocalDate.now().plusDays(2L))
                .endDate(LocalDate.now().plusDays(5L)).startPrice(50000)
                .build();
        //when
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(originalContract));
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(expectedContract));
        ContractDTO actualContractDTO = service.update(expectedContractDTO);
        //then
        assertContractEquals(expectedContractDTO, actualContractDTO);
    }

    @Test
    void add_validData_shouldReturnNewCreatedContract() {
        //given
        User contractOwner = User.builder().id(1).build();

        Contract expectedContract = Contract.builder()
                .id(3).owner(contractOwner).description("new contract").startDate(LocalDate.now().plusDays(1L))
                .endDate(LocalDate.now().plusDays(2L)).startPrice(50000).build();
        ContractDTO expectedContractDTO = ContractDTO.builder()
                .id(3).ownerId(1).description("new contract").startDate(LocalDate.now().plusDays(1L))
                .endDate(LocalDate.now().plusDays(2L)).startPrice(50000).build();
        //when
        when(contractRepository.save(expectedContract)).thenReturn(expectedContract);
        ContractDTO actualContract = service.add(expectedContractDTO).get();
        //then
        assertContractEquals(expectedContractDTO, actualContract);
    }

    private void assertContractEquals(ContractDTO expectedContract, ContractDTO actualContract) {

        assertAll(
                () -> assertEquals(expectedContract.getId(), actualContract.getId()),
                () -> assertEquals(expectedContract.getOwnerId(), actualContract.getOwnerId()),
                () -> assertEquals(expectedContract.getDescription(), actualContract.getDescription()),
                () -> assertEquals(expectedContract.getStartDate(), actualContract.getStartDate()),
                () -> assertEquals(expectedContract.getEndDate(), actualContract.getEndDate()),
                () -> assertEquals(expectedContract.getStartPrice(), actualContract.getStartPrice())
        );
    }
}