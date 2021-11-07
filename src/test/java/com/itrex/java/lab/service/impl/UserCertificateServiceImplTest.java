package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.UserCertificateRepository;
import com.itrex.java.lab.service.TestServiceConfiguration;
import com.itrex.java.lab.service.UserCertificateService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
class UserCertificateServiceImplTest {

    @Autowired
    private UserCertificateService service;
    @Autowired
    private UserCertificateRepository repository;

    @Test
    void assignCertificate_validDate_shouldReturnNewCertificate() throws RepositoryException, ServiceException {
        //given
        int userId = 3;
        int certificateId = 2;
        String certificateName = "Installation of external networks and structures";
        //when
        Mockito.when(repository.assignCertificate(userId, certificateId))
                .thenReturn(Optional.of(new Certificate(certificateId, certificateName)));
        Optional<Certificate> actual = service.assignCertificate(userId, certificateId);
        //then
        assertEquals(certificateId, actual.get().getId());
        assertEquals(certificateName, actual.get().getName());
    }

    @Test
    void removeCertificate_validData_shouldDeleteUserCertificate() throws RepositoryException, ServiceException {
        //given
        int userId = 3;
        int certificateId = 1;
        //when
        Mockito.when(repository.removeCertificate(userId, certificateId)).thenReturn(true);
        //then
        assertTrue(service.removeCertificate(userId, certificateId));
    }

    @Test
    void removeCertificate_invalidData_shouldReturnFalse() throws RepositoryException, ServiceException {
        //given
        int userId = 3;
        int certificateId = 2;
        // when
        Mockito.when(repository.removeCertificate(userId, certificateId)).thenReturn(false);
        //then
        assertFalse(service.removeCertificate(userId, certificateId));
    }

    @Test
    void findAllForUser_validData_shouldReturnCertificateList() throws RepositoryException, ServiceException {
        //given
        int expectedListSize = 2;
        //when
        Mockito.when(repository.findAllForUser(4)).thenReturn(Arrays.asList(new Certificate(), new Certificate()));
        List<Certificate> actualList = service.findAllForUser(4);
        //then
        assertEquals(expectedListSize, actualList.size());
    }

}