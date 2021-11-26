package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.repository.data.CertificateRepository;
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
    public List<CertificateDTO> findAllForUser(int userId) {

        return repository.findAllByUserId(userId).stream()
                .map(this::convertIntoCertificateDTO)
                .collect(Collectors.toList());
    }

    private CertificateDTO convertIntoCertificateDTO(Certificate certificate) {
        return mapper.map(certificate, CertificateDTO.class);
    }
}
