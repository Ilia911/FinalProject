package com.itrex.java.lab.repository.impl;


import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository{

    private DataSource dataSource;

    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String PASSWORD_COLUMN = "password";
    private static final String ROLE_COLUMN = "role_id";
    private static final String EMAIL_COLUMN = "email";
    private static final String FIND_USER_QUERY = "SELECT * FROM builder.user where id = ?";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM builder.user";
    private static final String DELETE_USER_QUERY
            = "DELETE FROM builder.user where id = ?;";
    private static final String UPDATE_USER_QUERY
            = "UPDATE builder.user SET name = ?, password = ?, role_id = ?, email = ? WHERE id = ?";
    private static final String ADD_USER_QUERY
            = "INSERT INTO builder.user(name, password, role_id, email, ) VALUES (?, ?, ?, ?)";

    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User find(int id) {
        try(Connection conn = dataSource.getConnection(); Statement st = conn.createStatement()) {
            ResultSet resultSet = st.executeQuery(FIND_USER_QUERY);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try(Connection conn = dataSource.getConnection(); Statement st = conn.createStatement()) {
            ResultSet resultSet = st.executeQuery(FIND_ALL_USERS_QUERY);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(ID_COLUMN));
                user.setName(resultSet.getString(NAME_COLUMN));

                //todo complete this method

                userList.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userList;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User add(User user) {
        return null;
    }
}
