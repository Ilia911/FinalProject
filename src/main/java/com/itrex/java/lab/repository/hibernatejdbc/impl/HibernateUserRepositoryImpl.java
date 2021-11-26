package com.itrex.java.lab.repository.hibernatejdbc.impl;

import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.hibernatejdbc.UserRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Deprecated
public class HibernateUserRepositoryImpl implements UserRepository {

    private static final String FIND_USERS_QUERY = "select u from User u ";
    private static final String FIND_USER_BY_EMAIL_QUERY = "select u from User u where email =:email";

    private EntityManager entityManager;

    @Override
    public Optional<User> findByEmail(String email) throws RepositoryException {

        User user;
        try {
            user = entityManager.createQuery(FIND_USER_BY_EMAIL_QUERY, User.class)
                    .setParameter("email", email).getSingleResult();
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findById(int id) throws RepositoryException {
        User user;
        try {
            user = entityManager.find(User.class, id);
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() throws RepositoryException {
        List<User> userList;
        try {
            userList = entityManager.createQuery(FIND_USERS_QUERY, User.class).getResultList();
            return userList;
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
    }

    @Override
    public boolean delete(int id) throws RepositoryException {
        boolean result;
        try {
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
                result = entityManager.find(User.class, id) == null;
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
            updatedUser = entityManager.merge(user);
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return updatedUser;
    }

    @Override
    public Optional<User> add(User user) throws RepositoryException {
        User newUser;
        try {
            Session session = entityManager.unwrap(Session.class);
            Integer newUserId = (Integer) session.save("User", user);
            newUser = session.find(User.class, newUserId);
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return Optional.ofNullable(newUser);
    }
}
