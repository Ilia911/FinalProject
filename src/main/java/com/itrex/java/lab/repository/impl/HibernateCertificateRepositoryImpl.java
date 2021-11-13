package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.CertificateRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RepositoryException.class)
@AllArgsConstructor
public class HibernateCertificateRepositoryImpl implements CertificateRepository {

    private static final String FIND_CERTIFICATES_BY_USER_ID_QUERY = "select * from builder.certificate c where c.id " +
            "in (select uc.certificate_id from builder.user_certificate uc where uc.user_id = :userId)";

    private final SessionFactory sessionFactory;

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
