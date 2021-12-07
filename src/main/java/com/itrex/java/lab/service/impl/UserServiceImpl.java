package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.entity.dto.RoleDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.data.CertificateRepository;
import com.itrex.java.lab.repository.data.ContractRepository;
import com.itrex.java.lab.repository.data.OfferRepository;
import com.itrex.java.lab.repository.data.UserRepository;
import com.itrex.java.lab.service.ContractService;
import com.itrex.java.lab.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final OfferRepository offerRepository;
    private final ContractRepository contractRepository;
    private final ContractService contractService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(this::convertUserToUserDTO);

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(this::convertUserToUserDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> pageUsers = userRepository.findAll(pageable);
        List<UserDTO> users = pageUsers.stream().map(this::convertUserToUserDTO).collect(Collectors.toList());
        return new PageImpl<>(users);
    }

    @Override
    @Transactional
    public boolean delete(int id) {

        List<Offer> userOffers = offerRepository.findByOfferOwnerId(id);
        for (Offer userOffer : userOffers) {
            offerRepository.deleteById(userOffer.getId());
        }
        List<Contract> userContracts = contractRepository.findAllByOwnerId(id);
        for (Contract userContract : userContracts) {
            contractService.delete(userContract.getId());
        }
        userRepository.deleteById(id);
        return userRepository.findById(id).isEmpty();
    }

    @Override
    @Transactional
    public UserDTO update(UserDTO userDTO) throws ServiceException {

        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (userDTO.getName() != null) {
                user.setName(userDTO.getName());
            }
            if (userDTO.getPassword() != null) {
                user.setPassword(userDTO.getPassword());
            }
            if (userDTO.getEmail() != null) {
                user.setEmail(userDTO.getEmail());
            }
            if (userDTO.getRole() != null) {
                user.setRole(convertRoleDTOtoRole(userDTO.getRole()));
            }
            userRepository.flush();
            return convertUserToUserDTO(userRepository.findById(userDTO.getId()).get());
        } else {
            throw new ServiceException("User do not exist");
        }
    }

    @Override
    @Transactional
    public Optional<UserDTO> add(UserDTO userDTO) {

        User user = convertUserDTOToUser(userDTO);
        User createdUser = userRepository.save(user);

        return Optional.ofNullable(convertUserToUserDTO(createdUser));
    }

    @Override
    @Transactional
    public List<CertificateDTO> assignCertificate(int userId, int certificateId) {

        Optional<Certificate> optionalCertificate = certificateRepository.findById(certificateId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalCertificate.isPresent() && optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getCertificates().stream().noneMatch(certificate -> certificate.getId() == certificateId)) {
                user.getCertificates().add(optionalCertificate.get());
                userRepository.flush();
            }
        }
        return certificateRepository.findAllByUserId(userId).stream().map(this::convertCertificateToCertificateDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CertificateDTO> removeCertificate(int userId, int certificateId) {

        List<CertificateDTO> resultList;
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Certificate> certificates = user.getCertificates().stream()
                    .filter((certificate -> certificate.getId() != certificateId))
                    .collect(Collectors.toList());
            user.setCertificates(certificates);
            userRepository.flush();
            Optional<User> updatedUser = userRepository.findById(userId);
            resultList = updatedUser.get().getCertificates().stream()
                    .map(this::convertCertificateToCertificateDTO)
                    .collect(Collectors.toList());
        } else {
            resultList = new ArrayList<>();
        }
        return resultList;
    }

    private UserDTO convertUserToUserDTO(User user) {
        modelMapper.typeMap(User.class, UserDTO.class);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setPassword("hidden");
        return userDTO;
    }

    private User convertUserDTOToUser(UserDTO user) {
        return modelMapper.map(user, User.class);
    }

    private CertificateDTO convertCertificateToCertificateDTO(Certificate certificate) {
        return modelMapper.map(certificate, CertificateDTO.class);
    }

    private Role convertRoleDTOtoRole(RoleDTO roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }
}
