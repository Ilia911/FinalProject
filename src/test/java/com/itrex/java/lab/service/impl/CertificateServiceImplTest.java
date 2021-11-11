package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.CertificateRepository;
import com.itrex.java.lab.service.CertificateService;
import com.itrex.java.lab.service.TestServiceConfiguration;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
class CertificateServiceImplTest {

    @Autowired
    private CertificateService service;
    @Autowired
    private CertificateRepository repository;

    @Test
    void findAllForUser_validData_shouldReturnCertificateList() throws RepositoryException, ServiceException {
        //given
        int expectedListSize = 2;
        //when
        Mockito.when(repository.findAllForUser(4))
                .thenReturn(Arrays.asList(Certificate.builder().build(), Certificate.builder().build()));
        List<CertificateDTO> actualList = service.findAllForUser(4);
        //then
        assertEquals(expectedListSize, actualList.size());
    }

}