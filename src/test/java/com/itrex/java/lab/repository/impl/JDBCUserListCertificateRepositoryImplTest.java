package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.JDBCUserListCertificateRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class JDBCUserListCertificateRepositoryImplTest extends BaseRepositoryTest {

    private final JDBCUserListCertificateRepository repository;

    public JDBCUserListCertificateRepositoryImplTest() {
        super();
        this.repository = new JDBCUserListCertificateRepositoryImpl(getConnectionPool());
    }

    @Test
    void assignCertificate_validDate_shouldReturnNewCertificate() {
        Certificate expected = new Certificate(2, "Installation of external networks and structures");

        Optional<Certificate> actual = repository.assignCertificate(3, 2);

        assertEquals(expected, actual.get());
    }

    @Test
    void removeCertificate_validData_shouldDeleteUser() {

        assertTrue(repository.removeCertificate(3, 1));
    }

    @Test
    void removeCertificate_invalidData_shouldDeleteUser() {

        assertFalse(repository.removeCertificate(3, 2));
    }

    @Test
    void findAllForUser_validData_shouldReturnCertificateList() {
        List<Certificate> expectedList = new ArrayList<>();
        Certificate expected1 = new Certificate(4, "Execution of works on the arrangement of road surfaces " +
                "of pedestrian zones from sidewalk slabs");
        Certificate expected2 = new Certificate(5, "Execution of works on the construction of insulating coatings");
        expectedList.add(expected1);
        expectedList.add(expected2);

        List<Certificate> actualList = repository.findAllForUser(4);

        assertEquals(expectedList, actualList);
    }
}