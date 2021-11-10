package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.CertificateRepository;
import com.itrex.java.lab.service.CertificateService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository repository;

    @Override
    @Transactional(readOnly = true)
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
