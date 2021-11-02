package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateUserRepositoryImpl implements UserRepository {

    @Autowired
    private final Session session;
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

        boolean result = false;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User user = session.find(User.class, id);
            if (user != null) {
                session.delete(user);
                result = session.find(User.class, id) == null;
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return result;
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
