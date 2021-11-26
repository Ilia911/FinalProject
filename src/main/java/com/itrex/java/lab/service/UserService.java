package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.exeption.ServiceException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserDTO> findById(int id);

    Optional<UserDTO> findByEmail(String email);

    List<UserDTO> findAll();

    boolean delete(int id);

    UserDTO update(UserDTO user) throws ServiceException;

    Optional<UserDTO> add(UserDTO user);

    List<CertificateDTO> assignCertificate(int userId, int certificateId);

    List<CertificateDTO> removeCertificate(int userId, int certificateId);
}
