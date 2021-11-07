package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.UserCertificateRepository;
import com.itrex.java.lab.service.UserCertificateService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserCertificateServiceImpl implements UserCertificateService {

    private final UserCertificateRepository repository;

    public UserCertificateServiceImpl(
            @Qualifier("hibernateUserCertificateRepositoryImpl") UserCertificateRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Certificate> assignCertificate(int userId, int certificateId) throws ServiceException {
        Optional<Certificate> createdCertificate;
        try {
            createdCertificate = repository.assignCertificate(userId, certificateId);
        } catch (RepositoryException ex) {
           throw new ServiceException(ex.getMessage(), ex);
        }
        return createdCertificate;
    }

    @Override
    public boolean removeCertificate(int userId, int certificateId) throws ServiceException {
        boolean result;
        try {
            result = repository.removeCertificate(userId, certificateId);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    public List<Certificate> findAllForUser(int userId) throws ServiceException {
        List<Certificate> allForUser;
        try {
            allForUser = repository.findAllForUser(userId);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return allForUser;
    }
}
