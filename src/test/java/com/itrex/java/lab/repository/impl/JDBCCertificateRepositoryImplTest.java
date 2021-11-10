package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.CertificateRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JDBCCertificateRepositoryImplTest extends BaseRepositoryTest {

    @Qualifier("JDBCCertificateRepositoryImpl")
    @Autowired
    private CertificateRepository repository;

    @Test
    void findAllForUser_validData_shouldReturnCertificateList() throws RepositoryException {
        //given
        int expectedListSize = 2;
        //when
        List<Certificate> actualList = repository.findAllForUser(4);
        //then
        assertEquals(expectedListSize, actualList.size());
    }

    @Test
    void findById_validData_shouldReturnCertificate() throws RepositoryException {
        //given
        Certificate expectedCertificate = Certificate.builder().id(2).name("Installation of external networks and structures").build();
        //when
        Certificate actualCertificate = repository.findById(2).get();
        //then
        assertAll(
                () -> assertEquals(expectedCertificate.getId(), actualCertificate.getId()),
                () -> assertEquals(expectedCertificate.getName(), actualCertificate.getName())
        );
    }
}