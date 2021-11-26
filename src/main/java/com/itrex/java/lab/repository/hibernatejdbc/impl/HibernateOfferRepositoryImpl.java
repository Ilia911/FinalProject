package com.itrex.java.lab.repository.hibernatejdbc.impl;

import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.hibernatejdbc.OfferRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Deprecated
public class HibernateOfferRepositoryImpl implements OfferRepository {

    private static final String FIND_OFFERS_BY_CONTRACT_ID_QUERY
            = "select o from Offer o where o.contract.id = :contractId";
    private static final String FIND_OFFERS_BY_USER_ID_QUERY
            = "select o from Offer o where o.offerOwner.id = :userId";

    private final EntityManager entityManager;

    @Override
    public Optional<Offer> find(int id) throws RepositoryException {
        Offer offer;
        try {
            offer = entityManager.find(Offer.class, id);
        } catch (Exception ex) {
            throw new RepositoryException("Can not find offer", ex);
        }
        return Optional.ofNullable(offer);
    }

    @Override
    public List<Offer> findAll(int contractId) throws RepositoryException {
        List<Offer> offers;
        try {
            offers = entityManager.createQuery(FIND_OFFERS_BY_CONTRACT_ID_QUERY, Offer.class)
                    .setParameter("contractId", contractId).getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("Can not find offer", ex);
        }
        return offers;
    }

    @Override
    public List<Offer> findAllByUserId(int userId) throws RepositoryException {
        List<Offer> offers;
        try {
            offers = entityManager.createQuery(FIND_OFFERS_BY_USER_ID_QUERY, Offer.class)
                    .setParameter("userId", userId).getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("Can not find offer", ex);
        }
        return offers;
    }

    @Override

    public boolean delete(int id) throws RepositoryException {
        boolean result;
        try {
            Offer offer = entityManager.find(Offer.class, id);

            if (offer != null) {
                entityManager.remove(offer);
                result = (null == entityManager.find(Offer.class, id));
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
            updatedOffer = entityManager.merge(offer);
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
            Session session = entityManager.unwrap(Session.class);
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
}
