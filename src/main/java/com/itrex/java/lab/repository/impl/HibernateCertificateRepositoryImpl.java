package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.CertificateRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RepositoryException.class)
@AllArgsConstructor
@Primary
public class HibernateCertificateRepositoryImpl implements CertificateRepository {

    private final SessionFactory sessionFactory;

    @Override
    public List<Certificate> findAllForUser(int userId) throws RepositoryException {
        List<Certificate> certificates;
        try {
            Session session = sessionFactory.getCurrentSession();
            certificates = session.createQuery("SELECT c FROM User u\n" +
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
            Session session = sessionFactory.getCurrentSession();
            certificate = session.find(Certificate.class, id);
        } catch (Exception ex) {
            throw new RepositoryException(ex.getMessage(), ex);
        }
        return Optional.ofNullable(certificate);
    }
}
