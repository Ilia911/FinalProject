package com.itrex.java.lab.repository.impl;


import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final DataSource dataSource;

    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String PASSWORD_COLUMN = "password";
    private static final String ROLE_COLUMN = "role_id";
    private static final String EMAIL_COLUMN = "email";
    private static final String FIND_USER_BY_EMAIL_QUERY = "SELECT * FROM builder.user where email = ?";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM builder.user where id = ?";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM builder.user";
    private static final String DELETE_USER_QUERY
            = "DELETE FROM builder.user where id = ?;";
    private static final String UPDATE_USER_QUERY
            = "UPDATE builder.user SET name = ?, password = ?, role_id = ?, email = ? WHERE id = ?";
    private static final String ADD_USER_QUERY
            = "INSERT INTO builder.user(name, password, role_id, email ) VALUES (?, ?, ?, ?)";
    private static final String REMOVE_ALL_USER_CONTRACTS_QUERY = "DELETE FROM builder.contract where owner_id = ?";
    private static final String REMOVE_ALL_USER_OFFERS_QUERY = "DELETE FROM builder.offer where offer_owner_id = ?";
    private static final String REMOVE_ALL_OFFERS_FOR_USER_CONTRACT_QUERY
            = "DELETE FROM builder.offer where contract_id = any (select contract_id from BUILDER.OFFER " +
            "join (select id from BUILDER.CONTRACT where OWNER_ID = ?) as ubc on CONTRACT_ID = ubc.id)";
    private static final String REMOVE_ALL_USER_CERTIFICATES_QUERY = "DELETE FROM builder.user_list_certificate where user_id = ?";


    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> find(String email) {
        try (Connection conn = dataSource.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(FIND_USER_BY_EMAIL_QUERY)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User(resultSet.getInt(ID_COLUMN),
                        resultSet.getString(NAME_COLUMN),
                        resultSet.getString(PASSWORD_COLUMN),
                        resultSet.getInt(ROLE_COLUMN),
                        resultSet.getString(EMAIL_COLUMN));
                return Optional.of(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection(); Statement st = conn.createStatement()) {
            ResultSet resultSet = st.executeQuery(FIND_ALL_USERS_QUERY);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(ID_COLUMN));
                user.setName(resultSet.getString(NAME_COLUMN));
                user.setRole(resultSet.getInt(ROLE_COLUMN));
                user.setEmail(resultSet.getString(EMAIL_COLUMN));
                userList.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userList;
    }

    @Override
    public boolean delete(int id) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            removeAllUserCertificates(conn, id);
            removeAllOffersForUserContract(conn, id);
            removeAllUserOffers(conn, id);
            removeAllUserContracts(conn, id);
            PreparedStatement preparedStatement = conn.prepareStatement(DELETE_USER_QUERY);
            preparedStatement.setInt(1, id);
            boolean result = preparedStatement.executeUpdate() == 1;
            conn.setAutoCommit(true);
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public User update(User user) {
        User updatedUser = null;
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER_QUERY);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getRole());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setInt(5, user.getId());
            preparedStatement.execute();
            updatedUser = find(user.getId(), conn);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return updatedUser;
    }

    @Override
    public Optional<User> add(User user) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(ADD_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getRole());
            preparedStatement.setString(4, user.getEmail());

            int effectiveRows = preparedStatement.executeUpdate();

            if (effectiveRows == 1) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return Optional.of(find(generatedKeys.getInt(ID_COLUMN), conn));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    private User find(int id, Connection conn) throws SQLException {
        User user = new User();
        PreparedStatement preparedStatement = conn.prepareStatement(FIND_USER_BY_ID_QUERY);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            user.setId(resultSet.getInt(ID_COLUMN));
            user.setName(resultSet.getString(NAME_COLUMN));
            user.setPassword(resultSet.getString(PASSWORD_COLUMN));
            user.setRole(resultSet.getInt(ROLE_COLUMN));
            user.setEmail(resultSet.getString(EMAIL_COLUMN));
        }
        return user;
    }

    private void removeAllUserContracts(Connection conn, int userId) throws SQLException {
        try {
            final PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_ALL_USER_CONTRACTS_QUERY);
            preparedStatement.setInt(1, userId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private void removeAllUserOffers(Connection conn, int offerOwnerId) throws SQLException {
        try {
            final PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_ALL_USER_OFFERS_QUERY);
            preparedStatement.setInt(1, offerOwnerId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private void removeAllOffersForUserContract(Connection conn, int offerOwnerId) throws SQLException {
        try {
            final PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_ALL_OFFERS_FOR_USER_CONTRACT_QUERY);
            preparedStatement.setInt(1, offerOwnerId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private void removeAllUserCertificates(Connection conn, int offerOwnerId) throws SQLException {
        try {
            final PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_ALL_USER_CERTIFICATES_QUERY);
            preparedStatement.setInt(1, offerOwnerId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
