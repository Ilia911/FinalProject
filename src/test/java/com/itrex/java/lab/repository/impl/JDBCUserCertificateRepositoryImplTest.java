package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.TestRepositoryConfiguration;
import com.itrex.java.lab.repository.UserCertificateRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
class JDBCUserCertificateRepositoryImplTest extends BaseRepositoryTest {

    @Qualifier("JDBCUserCertificateRepositoryImpl")
    @Autowired
    private UserCertificateRepository repository;

    @Test
    void assignCertificate_validDate_shouldReturnNewCertificate() throws RepositoryException {
        //given
        int userId = 3;
        int certificateId = 2;
        String certificateName = "Installation of external networks and structures";
        //when
        Optional<Certificate> actual = repository.assignCertificate(userId, certificateId);
        //then
        assertEquals(certificateId, actual.get().getId());
        assertEquals(certificateName, actual.get().getName());
    }

    @Test
    void removeCertificate_validData_shouldDeleteUser() throws RepositoryException {
        //given && when
        int userId = 3;
        int certificateId = 1;
        //then
        assertTrue(repository.removeCertificate(userId, certificateId));
    }

    @Test
    void removeCertificate_invalidData_shouldDeleteUser() throws RepositoryException {
        //when
        int userId = 3;
        int certificateId = 2;
        //then
        assertFalse(repository.removeCertificate(userId, certificateId));
    }

    @Test
    void findAllForUser_validData_shouldReturnCertificateList() throws RepositoryException {
        //given
        int expectedListSize = 2;
        //when
        List<Certificate> actualList = repository.findAllForUser(4);
        //then
        assertEquals(expectedListSize, actualList.size());
    }
}