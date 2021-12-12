package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.data.CertificateRepository;
import com.itrex.java.lab.repository.data.ContractRepository;
import com.itrex.java.lab.repository.data.OfferRepository;
import com.itrex.java.lab.repository.data.UserRepository;
import com.itrex.java.lab.service.ContractService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private OfferRepository offerRepository;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private ContractService contractService;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void find_validData_shouldReturnUser() {
        //given
        int userId = 1;
        User user = User.builder().id(userId).name("Customer").email("castomer@gmail.com").build();
        UserDTO expectedUserDTO = UserDTO.builder().id(userId).name("Customer").email("castomer@gmail.com").build();
        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedUserDTO);
        Optional<UserDTO> actualUser = userService.findById(userId);
        //then
        assertUserEquals(expectedUserDTO, actualUser.get());
    }

    @Test
    void findByEmail_validData_shouldReturnUser() {
        //given
        int userId = 1;
        String userName = "Customer";
        String userPassword = "password";
        String userEmail = "castomer@gmail.com";
        User user = User.builder().id(userId).name(userName).password(userPassword).email(userEmail).build();
        UserDTO expectedUserDTO = UserDTO.builder().id(userId).name(userName).password(userPassword).email(userEmail).build();
        // when
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedUserDTO);
        Optional<UserDTO> actualUser = userService.findByEmail(userEmail);
        //then
        assertUserEquals(expectedUserDTO, actualUser.get());
    }

    @Test
    void findAll_validData_shouldReturnUserList() {
        //given
        int expectedSize = 2;
        User user = User.builder().build();
        UserDTO userDTO = UserDTO.builder().build();
        Pageable pageable = PageRequest.of(1, 2, Sort.by("name").descending());
        // when
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(user, user)));
        int actualSize = userService.findAll(pageable).getSize();
        //then
        assertEquals(expectedSize, actualSize);
    }

    @Test
    void delete_validUserWithContract_shouldReturnTrue() {
        //given
        int validId = 1;
        int contractId = 1;
        // when
        when(offerRepository.findByOfferOwnerId(validId)).thenReturn(List.of());
        when(contractRepository.findAllByOwnerId(validId)).thenReturn(List.of(Contract.builder().id(contractId).build()));
        when(contractService.delete(contractId)).thenReturn(true);
        when(userRepository.findById(validId)).thenReturn(Optional.empty());
        //then
        assertTrue(userService.delete(validId));
    }

    @Test
    void delete_validUserWithOffer_shouldReturnTrue() {
        //given
        int validId = 3;
        int offerId = 1;
        Offer offer = Offer.builder().id(1).contract(Contract.builder().id(offerId).build()).build();
        // when
        when(offerRepository.findByOfferOwnerId(validId)).thenReturn(List.of(offer));
        when(contractRepository.findAllByOwnerId(validId)).thenReturn(List.of());
        when(userRepository.findById(validId)).thenReturn(Optional.empty());
        //then
        assertTrue(userService.delete(validId));
    }

    @Test
    void update_validData_shouldReturnUpdatedUserDTO() throws ServiceException {
        //given
        int userId = 1;
        String name = "updatedName";
        UserDTO userDTO = UserDTO.builder().id(userId).name(name).password("pass").role(Role.CONTRACTOR).email("email").build();
        User user = User.builder().id(userId).name(name).password("pass").role(Role.CONTRACTOR).email("email").build();
        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDTO actualUpdatedUser = userService.update(userDTO);
        // then
        assertAll(() -> assertEquals(userId, actualUpdatedUser.getId()),
                () -> assertEquals(name, actualUpdatedUser.getName()),
                () -> assertEquals(Role.CONTRACTOR, actualUpdatedUser.getRole())
        );
    }

    @Test
    void update_inValidData_shouldThrowServiceException() {
        //given
        int invalidUserId = 9;
        UserDTO absentUserDTO = UserDTO.builder().id(invalidUserId).build();
        //when
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());
        // then
        assertThrows(ServiceException.class, () -> userService.update(absentUserDTO));
    }

    @Test
    void add_validData_shouldReturnNewUserDTO() {
        //given
        UserDTO userDTO = UserDTO.builder().id(5).name("newUser").password("pass").role(Role.CUSTOMER).email("email").build();
        User user = User.builder().id(5).name("newUser").password("pass").role(Role.CUSTOMER).email("email").build();
        //when
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO actualUserDTO = userService.add(userDTO).get();
        //then
        assertAll(() -> assertEquals(user.getId(), actualUserDTO.getId()),
                () -> assertEquals(user.getName(), actualUserDTO.getName()),
                () -> assertEquals(user.getRole(), actualUserDTO.getRole()));
    }

    @Test
    void assignCertificate_validDate_shouldReturnCertificateList() {
        //given
        int userId = 3;
        int certificateId = 2;
        //when
        when(certificateRepository.findById(certificateId))
                .thenReturn(Optional.of(Certificate.builder().id(certificateId).build()));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(User.builder().id(userId).certificates(new ArrayList<>()).build()));
        when(certificateRepository.findAllByUserId(userId))
                .thenReturn(Arrays.asList(Certificate.builder().build(), Certificate.builder().build(),
                        Certificate.builder().build()));

        List<CertificateDTO> actualCertificates = userService.assignCertificate(userId, certificateId);
        //then
        assertEquals(3, actualCertificates.size());

    }

    @Test
    void removeCertificate_validData_shouldDeleteUserCertificate() {
        //given
        int userId = 3;
        int certificateId = 1;
        User user = User.builder().id(userId).certificates(new ArrayList<>()).build();
        //when
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(User.builder()
                        .id(userId)
                        .certificates(List.of(Certificate.builder().id(certificateId).build())).build()));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        //then
        assertEquals(0, userService.removeCertificate(userId, certificateId).size());
    }

    private void assertUserEquals(UserDTO expectedUser, UserDTO actualUser) {
        assertAll(
                () -> assertEquals(expectedUser.getId(), actualUser.getId()),
                () -> assertEquals(expectedUser.getName(), actualUser.getName()),
                () -> assertEquals(expectedUser.getEmail(), actualUser.getEmail())
        );
    }
}