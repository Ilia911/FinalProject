package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.CertificateRepository;
import com.itrex.java.lab.service.CertificateService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository repository;
    private final ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDTO> findAllForUser(int userId) throws ServiceException {
        List<Certificate> allForUser;
        try {
            return repository.findAllForUser(userId).stream()
                    .map(this::convertIntoCertificateDTO)
                    .collect(Collectors.toList());
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    private CertificateDTO convertIntoCertificateDTO(Certificate certificate) {
        return mapper.map(certificate, CertificateDTO.class);
    }
}
