package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.User;
import java.util.List;

public interface UserRepository {

    List<User> findAll();

    boolean delete(int id);

    User update(User user);

    User add(User user);
}
