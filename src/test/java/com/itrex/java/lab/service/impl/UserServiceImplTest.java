package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.entity.dto.RoleDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.CertificateRepository;
import com.itrex.java.lab.repository.ContractRepository;
import com.itrex.java.lab.repository.OfferRepository;
import com.itrex.java.lab.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    ContractService contractService;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void findAll_validData_shouldReturnUserList() throws ServiceException, RepositoryException {
        //given
        int expectedSize = 4;
        User user = User.builder().build();
        UserDTO userDTO = UserDTO.builder().build();
        // when
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user, user, user));
        int actualSize = userService.findAll().size();
        //then
        assertEquals(expectedSize, actualSize);
    }

    @Test
    void delete_validUserWithContract_shouldReturnTrue() throws RepositoryException, ServiceException {
        //given
        int validId = 1;
        int contractId = 1;
        // when
        when(offerRepository.findAllByUserId(validId)).thenReturn(List.of());
        when(contractRepository.findAllByUserId(validId)).thenReturn(List.of(Contract.builder().id(contractId).build()));
        when(contractService.delete(contractId)).thenReturn(true);
        when(userRepository.delete(validId)).thenReturn(true);
        //then
        assertTrue(userService.delete(validId));
    }

    @Test
    void delete_validUserWithOffer_shouldReturnTrue() throws RepositoryException, ServiceException {
        //given
        int validId = 3;
        int offerId = 1;
        Offer offer = Offer.builder().id(1).contract(Contract.builder().id(offerId).build()).build();
        // when
        when(offerRepository.findAllByUserId(validId)).thenReturn(List.of(offer));
        when(offerRepository.delete(offerId)).thenReturn(true);
        when(userRepository.delete(validId)).thenReturn(true);
        //then
        assertTrue(userService.delete(validId));
    }

    @Test
    void delete_invalidData_shouldReturnFalse() throws RepositoryException, ServiceException {
        //given && when
        int invalidId = 5;
        when(userRepository.delete(invalidId)).thenReturn(false);
        //then
        assertFalse(userService.delete(invalidId));
    }

    @Test
    void update_validData_shouldReturnUpdatedUserDTO() throws RepositoryException, ServiceException {
        //given
        int userId = 1;
        String name = "updatedName";
        RoleDTO roleDTO = RoleDTO.builder().id(3).name("contractor").build();
        Role role = Role.builder().id(3).name("contractor").build();
        UserDTO userDTO = UserDTO.builder().id(userId).name(name).password("pass").role(roleDTO).email("email").build();
        User user = User.builder().id(userId).name(name).password("pass").role(role).email("email").build();
        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(roleDTO, Role.class)).thenReturn(role);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userRepository.update(user)).thenReturn(user);
        UserDTO actualUpdatedUser = userService.update(userDTO);
        // then
        assertAll(() -> assertEquals(userId, actualUpdatedUser.getId()),
                () -> assertEquals(name, actualUpdatedUser.getName()),
                () -> assertEquals(role.getId(), actualUpdatedUser.getRole().getId())
        );
    }

    @Test
    void update_inValidData_shouldThrowServiceException() throws RepositoryException {
        //given
        int invalidUserId = 9;
        UserDTO absentUserDTO = UserDTO.builder().id(invalidUserId).build();
        //when
        when(userRepository.findById(invalidUserId)).thenReturn(Optional.empty());
        // then
        assertThrows(ServiceException.class, () -> userService.update(absentUserDTO));
    }

    @Test
    void add_validData_shouldReturnNewUserDTO() throws RepositoryException, ServiceException {
        //given
        UserDTO userDTO = UserDTO.builder().id(5).name("newUser").password("pass").role(RoleDTO.builder().id(2).name("customer").build()).email("email").build();
        User user = User.builder().id(5).name("newUser").password("pass").role(Role.builder().id(2).name("customer").build()).email("email").build();
        //when
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userRepository.add(user)).thenReturn(Optional.of(user));
        UserDTO actualUserDTO = userService.add(userDTO).get();
        //then
        assertAll(() -> assertEquals(user.getId(), actualUserDTO.getId()),
                () -> assertEquals(user.getName(), actualUserDTO.getName()),
                () -> assertEquals(user.getRole().getId(), actualUserDTO.getRole().getId()));
    }

    @Test
    void assignCertificate_validDate_shouldReturnCertificateList() throws RepositoryException, ServiceException {
        //given
        int userId = 3;
        int certificateId = 2;
        //when
        when(certificateRepository.findById(certificateId))
                .thenReturn(Optional.of(Certificate.builder().id(certificateId).build()));
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(User.builder().id(userId).certificates(new ArrayList<>()).build()));
        when(certificateRepository.findAllForUser(userId))
                .thenReturn(Arrays.asList(Certificate.builder().build(), Certificate.builder().build(),
                        Certificate.builder().build()));

        List<CertificateDTO> actualCertificates = userService.assignCertificate(userId, certificateId);
        //then
        assertEquals(3, actualCertificates.size());

    }

    @Test
    void removeCertificate_validData_shouldDeleteUserCertificate() throws RepositoryException, ServiceException {
        //given
        int userId = 3;
        int certificateId = 1;
        User user = User.builder().id(userId).certificates(new ArrayList<>()).build();
        //when
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(User.builder()
                        .id(userId)
                        .certificates(List.of(Certificate.builder().id(certificateId).build())).build()));
        when(userRepository.update(user)).thenReturn(user);
        //then
        assertEquals(0, userService.removeCertificate(userId, certificateId).size());
    }
}