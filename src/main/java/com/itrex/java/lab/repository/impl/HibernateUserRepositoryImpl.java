package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.UserRepository;
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
public class HibernateUserRepositoryImpl implements UserRepository {

    private static final String FIND_USERS_QUERY = "select u from User u ";
    private static final String FIND_USER_BY_EMAIL_QUERY = "select u from User u where email = ?0";
    @Autowired
    private SessionFactory sessionFactory;

    public HibernateUserRepositoryImpl() {
    }

    @Override
    public Optional<User> findByEmail(String email) throws RepositoryException {

        User user;
        try {
            Session session = sessionFactory.getCurrentSession();
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
            Session session = sessionFactory.getCurrentSession();
            userList = session.createQuery(FIND_USERS_QUERY, User.class).list();
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return userList;
    }

    @Override
    public boolean delete(int id) throws RepositoryException {
        boolean result;
        try {
            Session session = sessionFactory.getCurrentSession();
            User user = session.find(User.class, id);
            if (user != null) {
                session.delete(user);
                result = session.find(User.class, id) == null;
            } else {
                result = false;
            }
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return result;
    }

    @Override
    public User update(User user) throws RepositoryException {

        User updatedUser;
        try {
            Session session = sessionFactory.getCurrentSession();
            session.update("User", user);
            updatedUser = session.find(User.class, user.getId());
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return updatedUser;
    }

    @Override
    public Optional<User> add(User user) throws RepositoryException {
        User newUser;
        try {
            Session session = sessionFactory.getCurrentSession();
            Integer newUserId = (Integer) session.save("User", user);
            newUser = session.find(User.class, newUserId);
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return Optional.ofNullable(newUser);
    }

}
