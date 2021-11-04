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
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RepositoryException.class)
public class HibernateUserCertificateRepositoryImpl implements UserCertificateRepository {

    private static final String FIND_CERTIFICATES_BY_USER_ID_QUERY = "select * from builder.certificate c where c.id " +
            "in (select uc.certificate_id from builder.user_certificate uc where uc.user_id = :userId)";

    @Autowired
    private SessionFactory sessionFactory;

    public HibernateUserCertificateRepositoryImpl() {
    }

    @Override
    public Optional<Certificate> assignCertificate(int userId, int certificateId) throws RepositoryException {
        Certificate createdCertificate;
        try {
            Session session = sessionFactory.getCurrentSession();
            Certificate certificate = session.find(Certificate.class, certificateId);
            User user = session.find(User.class, userId);
            user.getCertificates().add(certificate);
            createdCertificate = user.getCertificates().stream()
                    .filter((cert) -> cert.getId() == certificateId).collect(Collectors.toList()).get(0);
        } catch (Exception ex) {
            throw new RepositoryException("Can not assign certificate", ex);
        }
        return Optional.ofNullable(createdCertificate);
    }

    @Override
    public boolean removeCertificate(int userId, int certificateId) throws RepositoryException {
        boolean result = false;
        try {
            Session session = sessionFactory.getCurrentSession();
            User user = session.find(User.class, userId);
            List<Certificate> certificates = user.getCertificates();
            Iterator<Certificate> iterator = certificates.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId() == certificateId) {
                    iterator.remove();
                    result = true;
                }
            }
        } catch (Exception ex) {
            throw new RepositoryException("Can not remove certificate", ex);
        }
        return result;
    }

    @Override
    public List<Certificate> findAllForUser(int userId) throws RepositoryException {
        List<Certificate> certificates;
        try {
            Session session = sessionFactory.getCurrentSession();
            certificates = (List<Certificate>) session.createSQLQuery(
                    FIND_CERTIFICATES_BY_USER_ID_QUERY)
                    .setParameter("userId", userId).list();
        } catch (Exception ex) {
            throw new RepositoryException("Can not find certificates certificate", ex);
        }
        return certificates;
    }
}
