package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email) throws RepositoryException;

    List<User> findAll() throws RepositoryException;

    boolean delete(int id) throws RepositoryException;

    User update(User user) throws RepositoryException;

    Optional<User> add(User user) throws RepositoryException;
}
