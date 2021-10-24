package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateUserRepositoryImpl implements UserRepository {

    private final Session session;
    private static final String REMOVE_ALL_OFFERS_FOR_USER_CONTRACT_QUERY
            = "DELETE FROM builder.offer where contract_id = any (select contract_id from BUILDER.OFFER " +
            "join (select id from BUILDER.CONTRACT where OWNER_ID = ?) as ubc on CONTRACT_ID = ubc.id)";
    private static final String REMOVE_USER_OFFERS_QUERY = "delete from Offer o where o.offerOwnerId = :ownerId";
    private static final String REMOVE_USER_CONTRACTS_QUERY = "delete from Contract c where c.ownerId = :ownerId";
    private static final String REMOVE_USER_QUERY = "delete from User u where id = :id";
    private static final String FIND_USERS_QUERY = "select u from User u ";
    private static final String FIND_USER_BY_EMAIL_QUERY = "select u from User u where email = ?0";


    public HibernateUserRepositoryImpl(Session session) {
        this.session = session;
    }

    @Override
    public Optional<User> findByEmail(String email) throws RepositoryException {
        User user;
        try {
            user = session.createQuery(FIND_USER_BY_EMAIL_QUERY, User.class)
                    .setParameter(0, email).uniqueResult();
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() throws RepositoryException {

        List<User> userList;
        try {
            userList = session.createQuery(FIND_USERS_QUERY, User.class).list();
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return userList;
    }

    @Override
    public boolean delete(int id) throws RepositoryException {

        int effectedRows;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.createQuery(REMOVE_USER_OFFERS_QUERY).setParameter("ownerId", id).executeUpdate();
            session.createSQLQuery(REMOVE_ALL_OFFERS_FOR_USER_CONTRACT_QUERY).setParameter(1, id).executeUpdate();
            session.createQuery(REMOVE_USER_CONTRACTS_QUERY)
                    .setParameter("ownerId", id).executeUpdate();
            effectedRows = session.createQuery(REMOVE_USER_QUERY)
                    .setParameter("id", id).executeUpdate();
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return effectedRows  > 0;
    }

    @Override
    public User update(User user) throws RepositoryException {

        User updatedUser;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update("User", user);
            updatedUser = session.find(User.class, user.getId());
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return updatedUser;
    }

    @Override
    public Optional<User> add(User user) throws RepositoryException {
        User newUser;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Integer newUserId = (Integer) session.save("User", user);
            newUser = session.find(User.class, newUserId);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return Optional.ofNullable(newUser);
    }
}
