package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.OfferRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateOfferRepositoryImpl implements OfferRepository {

    private final Session session;
    private static final String FIND_OFFERS_BY_CONTRACT_ID_QUERY
            = "select o from Offer o where o.contractId = :contractId";

    public HibernateOfferRepositoryImpl(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Offer> find(int id) throws RepositoryException {
        Offer offer;
        try {
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
            offers = session.createQuery(FIND_OFFERS_BY_CONTRACT_ID_QUERY)
                    .setParameter("contractId", contractId).getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("Can not find offer", ex);
        }
        return offers;
    }

    @Override
    public boolean delete(int id) throws RepositoryException {
        boolean result;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Offer offer = session.find(Offer.class, id);

            if (offer != null) {
                session.delete(offer);
                result = (null == session.find(Offer.class, id));
            } else {
                result = false;
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Can not remove 'Offer'", ex);
        }
        return result;
    }

    @Override
    public Offer update(Offer offer) throws RepositoryException {
        validateOfferData(offer);

        Offer updatedOffer;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update("Offer", offer);
            updatedOffer = session.find(Offer.class, offer.getId());
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Can not update offer!", ex);
        }
        return updatedOffer;
    }

    @Override
    public Optional<Offer> add(Offer offer) throws RepositoryException {
        validateOfferData(offer);

        Offer createdOffer;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            int newOfferId = (Integer) session.save("Offer", offer);
            createdOffer = session.find(Offer.class, newOfferId);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
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
}
