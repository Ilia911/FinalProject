package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.CertificateRepository;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() throws ServiceException {
        List<UserDTO> usersDTO;
        try {
            usersDTO = userRepository.findAll().stream().map(this::convertUserToUserDTO).collect(Collectors.toList());
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return usersDTO;
    }

    @Override
    @Transactional
    public boolean delete(int id) throws ServiceException {

        try {
            return userRepository.delete(id);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public UserDTO update(User user) throws ServiceException {
        UserDTO userDTO = null;
        try {
            User updatedUser = userRepository.update(user);
            userDTO = convertUserToUserDTO(updatedUser);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return userDTO;
    }

    @Override
    @Transactional
    public Optional<UserDTO> register(User user) throws ServiceException {
        UserDTO createdUserDTO = null;
        try {
            Optional<User> createdUser = userRepository.add(user);
            if (createdUser.isPresent()) {
                createdUserDTO = convertUserToUserDTO(createdUser.get());
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return Optional.ofNullable(createdUserDTO);
    }

    @Override
    @Transactional
    public List<CertificateDTO> assignCertificate(int userId, int certificateId) throws ServiceException {
        try {
            Optional<Certificate> optionalCertificate = certificateRepository.findById(certificateId);
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalCertificate.isPresent() && optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (user.getCertificates().stream().noneMatch(certificate -> certificate.getId() == certificateId)) {
                    user.getCertificates().add(optionalCertificate.get());
                    userRepository.update(user);
                }
            }
            return certificateRepository.findAllForUser(userId).stream().map(this::convertCertificateToCertificateDTO)
                    .collect(Collectors.toList());
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public List<CertificateDTO> removeCertificate(int userId, int certificateId) throws ServiceException {
        try {
            List<CertificateDTO> resultList;
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                List<Certificate> certificates = user.getCertificates().stream()
                        .filter((certificate -> certificate.getId() != certificateId))
                        .collect(Collectors.toList());
                user.setCertificates(certificates);
                userRepository.update(user);
                User updatedUser = userRepository.update(optionalUser.get());
                resultList = updatedUser.getCertificates().stream()
                        .map(this::convertCertificateToCertificateDTO)
                        .collect(Collectors.toList());
            } else {
                resultList = new ArrayList<>();
            }
            return resultList;
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    private UserDTO convertUserToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private CertificateDTO convertCertificateToCertificateDTO(Certificate certificate) {
        return modelMapper.map(certificate, CertificateDTO.class);
    }
}
