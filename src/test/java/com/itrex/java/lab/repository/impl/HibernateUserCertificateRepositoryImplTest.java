package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.UserCertificateRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernateUserCertificateRepositoryImplTest extends BaseRepositoryTest {

    private final UserCertificateRepository repository;

    public HibernateUserCertificateRepositoryImplTest() {
        super();
        this.repository = new HibernateUserCertificateRepositoryImpl(getSessionFactory().openSession());
    }

    @Test
    void assignCertificate_validDate_shouldReturnNewCertificate() throws RepositoryException {
        //given
        Certificate expected = new Certificate(2, "Installation of external networks and structures");
        //when
        int userId = 3;
        int certificateId = 2;
        Optional<Certificate> actual = repository.assignCertificate(userId, certificateId);
        //then
        assertEquals(expected, actual.get());
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
        List<Certificate> expectedList = new ArrayList<>();
        Certificate expected1 = new Certificate(4, "Execution of works on the arrangement of road surfaces " +
                "of pedestrian zones from sidewalk slabs");
        Certificate expected2 = new Certificate(5, "Execution of works on the construction of insulating coatings");
        expectedList.add(expected1);
        expectedList.add(expected2);
        //when
        List<Certificate> actualList = repository.findAllForUser(4);
        //then
        assertEquals(expectedList, actualList);
    }
}