package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.OfferRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = RepositoryException.class)
public class HibernateOfferRepositoryImpl implements OfferRepository {

    private static final String FIND_OFFERS_BY_CONTRACT_ID_QUERY
            = "select o from Offer o where o.contract.id = :contractId";
    @Autowired
    private SessionFactory sessionFactory;
    private Session session;

    public HibernateOfferRepositoryImpl() {
    }

    @Override
    public Optional<Offer> find(int id) throws RepositoryException {
        Offer offer;
        try {
            session = sessionFactory.getCurrentSession();
            offer = session.find(Offer.class, id);
        } catch (Exception ex) {
            throw new RepositoryException("Can not find offer", ex);
        }
        return Optional.ofNullable(offer);
    }

    @Override
    public List<Offer> findAll(int contractId) throws RepositoryException {
        List<Offer> offers;
        try {
            session = sessionFactory.getCurrentSession();
            offers = session.createQuery(FIND_OFFERS_BY_CONTRACT_ID_QUERY, Offer.class)
                    .setParameter("contractId", contractId).getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("Can not find offer", ex);
        }
        return offers;
    }

    @Override
    public boolean delete(int id) throws RepositoryException {
        boolean result;
        try {
            session = sessionFactory.getCurrentSession();
            Offer offer = session.find(Offer.class, id);

            if (offer != null) {
                session.delete(offer);
                result = (null == session.find(Offer.class, id));
            } else {
                result = false;
            }
        } catch (Exception ex) {
            throw new RepositoryException("Can not remove 'Offer'", ex);
        }
        return result;
    }

    @Override
    public Offer update(Offer offer) throws RepositoryException {
        validateOfferData(offer);

        Offer updatedOffer;
        try {
            session = sessionFactory.getCurrentSession();
            session.update("Offer", offer);
            updatedOffer = session.find(Offer.class, offer.getId());
        } catch (Exception ex) {
            throw new RepositoryException("Can not update offer!", ex);
        }
        return updatedOffer;
    }

    @Override
    public Optional<Offer> add(Offer offer) throws RepositoryException {
        validateOfferData(offer);

        Offer createdOffer;
        try {
            session = sessionFactory.getCurrentSession();
            int newOfferId = (Integer) session.save("Offer", offer);
            createdOffer = session.find(Offer.class, newOfferId);
        } catch (Exception ex) {
            throw new RepositoryException("Can not add offer", ex);
        }
        return Optional.ofNullable(createdOffer);
    }

    private void validateOfferData(Offer offer) throws RepositoryException {
        if (offer == null) {
            throw new RepositoryException("Offer can not be null!");
        }
        if (offer.getPrice() == null || offer.getPrice() <= 0) {
            throw new RepositoryException("Offer field 'price' must not be null or empty!");
        }
    }

    private void fetchSession() {
        if (session == null) {
            session = sessionFactory.openSession();
        }
    }
}
