package com.itrex.java.lab.repository.hibernatejdbc.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.hibernatejdbc.CertificateRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Deprecated
public class HibernateCertificateRepositoryImpl implements CertificateRepository {

    private final EntityManager entityManager;

    @Override
    public List<Certificate> findAllForUser(int userId) throws RepositoryException {
        List<Certificate> certificates;
        try {
            certificates = entityManager.createQuery("SELECT c FROM User u\n" +
                            "    JOIN u.certificates c \n" +
                            "    WHERE u.id =:userId", Certificate.class)
                    .setParameter("userId", userId)
                    .getResultList();
            System.out.println(certificates);
        } catch (Exception ex) {
            throw new RepositoryException("Can not find certificates", ex);
        }
        return certificates;
    }

    @Override
    public Optional<Certificate> findById(int id) throws RepositoryException {
        Certificate certificate;
        try {
            certificate = entityManager.find(Certificate.class, id);
        } catch (Exception ex) {
            throw new RepositoryException(ex.getMessage(), ex);
        }
        return Optional.ofNullable(certificate);
    }
}
