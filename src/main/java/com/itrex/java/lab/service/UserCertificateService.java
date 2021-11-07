package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.ServiceException;
import java.util.List;
import java.util.Optional;

public interface UserCertificateService {

    Optional<Certificate> assignCertificate(int userId, int certificateId) throws ServiceException;

    boolean removeCertificate(int userId, int certificateId) throws ServiceException;

    List<Certificate> findAllForUser(int userId) throws ServiceException;
}
