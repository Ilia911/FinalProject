package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> find(String email);

    List<User> findAll();

    boolean delete(int id);

    User update(User user);

    Optional<User> add(User user);
}
