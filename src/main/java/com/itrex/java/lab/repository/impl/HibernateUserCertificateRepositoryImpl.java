package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.UserCertificateRepository;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateUserCertificateRepositoryImpl implements UserCertificateRepository {

    @Autowired
    private final Session session;

    public HibernateUserCertificateRepositoryImpl(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Certificate> assignCertificate(int userId, int certificateId) throws RepositoryException {
        Certificate createdCertificate;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Certificate certificate = session.find(Certificate.class, certificateId);
            User user = session.find(User.class, userId);
            user.getCertificates().add(certificate);
            createdCertificate = user.getCertificates().stream()
                    .filter((cert) -> cert.getId() == certificateId).collect(Collectors.toList()).get(0);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Can not assign certificate", ex);
        }
        return Optional.ofNullable(createdCertificate);
    }

    @Override
    public boolean removeCertificate(int userId, int certificateId) throws RepositoryException {
        boolean result = false;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User user = session.find(User.class, userId);
            List<Certificate> certificates = user.getCertificates();
            Iterator<Certificate> iterator = certificates.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId() == certificateId) {
                    iterator.remove();
                    result = true;
                }
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Can not remove certificate", ex);
        }
        return result;
    }

    @Override
    public List<Certificate> findAllForUser(int userId) throws RepositoryException {
        List<Certificate> certificates;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User user = session.find(User.class, userId);
            certificates = user.getCertificates();
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Can not find certificates certificate", ex);
        }
        return certificates;
    }
}
