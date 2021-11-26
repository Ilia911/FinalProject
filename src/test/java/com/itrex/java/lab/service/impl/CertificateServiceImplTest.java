package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.repository.data.CertificateRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {

    @InjectMocks
    private CertificateServiceImpl service;
    @Mock
    private CertificateRepository repository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void findAllForUser_validData_shouldReturnCertificateList() {
        //given
        int expectedListSize = 2;
        Certificate certificate = Certificate.builder().build();
        CertificateDTO certificateDTO = CertificateDTO.builder().build();
        //when
        when(modelMapper.map(certificate, CertificateDTO.class)).thenReturn(certificateDTO);
        when(repository.findAllByUserId(4))
                .thenReturn(Arrays.asList(certificate, certificate));
        List<CertificateDTO> actualList = service.findAllForUser(4);
        //then
        assertEquals(expectedListSize, actualList.size());
    }
}