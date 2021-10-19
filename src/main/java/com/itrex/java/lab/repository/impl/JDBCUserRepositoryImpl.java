package com.itrex.java.lab.repository.impl;


import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.JDBCUserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

public class JDBCUserRepositoryImpl implements JDBCUserRepository {

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


    public JDBCUserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> find(String email) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_USER_BY_EMAIL_QUERY);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = createUser(resultSet, true);
            }
            return Optional.ofNullable(user);
        } catch (SQLException ex) {
            throw new RepositoryException("Something was wrong with the repository", ex);
        }
    }


    @Override
    public List<User> findAll() throws RepositoryException {
        try (Connection conn = dataSource.getConnection(); Statement st = conn.createStatement()) {
            ResultSet resultSet = st.executeQuery(FIND_ALL_USERS_QUERY);
            List<User> userList = new ArrayList<>();
            while (resultSet.next()) {
                User user = createUser(resultSet, false);
                userList.add(user);
            }
            return userList;
        } catch (SQLException ex) {
            throw new RepositoryException("Something was wrong with the repository", ex);
        }

    }

    @Override
    public boolean delete(int id) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            boolean result;
            try {
                conn.setAutoCommit(false);
                removeAllUserCertificates(conn, id);
                removeAllOffersForUserContract(conn, id);
                removeAllUserOffers(conn, id);
                removeAllUserContracts(conn, id);
                PreparedStatement preparedStatement = conn.prepareStatement(DELETE_USER_QUERY);
                preparedStatement.setInt(1, id);
                result = preparedStatement.executeUpdate() == 1;
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new RepositoryException("Something was wrong with the additional operations", ex);
            } finally {
                conn.setAutoCommit(true);
            }
            return result;
        } catch (SQLException ex) {
            throw new RepositoryException("Something was wrong with the connection", ex);
        }
    }

    @Override
    public User update(User user) throws RepositoryException {

        try (Connection conn = dataSource.getConnection()) {
            User updatedUser;
            conn.setAutoCommit(false);
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USER_QUERY);
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setInt(3, user.getRole());
                preparedStatement.setString(4, user.getEmail());
                preparedStatement.setInt(5, user.getId());
                preparedStatement.execute();
                updatedUser = find(user.getId(), conn);
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new RepositoryException("Something was wrong with the additional operations", ex);
            } finally {
                conn.setAutoCommit(true);
            }
            return updatedUser;
        } catch (SQLException ex) {
            throw new RepositoryException("Something was wrong with the connection", ex);
        }
    }

    @Override
    public Optional<User> add(User user) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                User newUser = null;
                PreparedStatement preparedStatement = conn.prepareStatement(ADD_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setInt(3, user.getRole());
                preparedStatement.setString(4, user.getEmail());

                int effectiveRows = preparedStatement.executeUpdate();

                if (effectiveRows == 1) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        newUser = find(generatedKeys.getInt(ID_COLUMN), conn);
                    }
                }
                conn.commit();
                return Optional.ofNullable(newUser);
            } catch (SQLException ex) {
                conn.rollback();
                throw new RepositoryException("Something was wrong with the additional operations", ex);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Something was wrong with the connection", ex);
        }
    }

    private User find(int id, Connection conn) throws SQLException {
        User user = new User();
        PreparedStatement preparedStatement = conn.prepareStatement(FIND_USER_BY_ID_QUERY);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            user = createUser(resultSet, true);
        }
        return user;
    }

    private User createUser(ResultSet rs, boolean isSetPassword) throws SQLException {
        User user = new User();

        user.setId(rs.getInt(ID_COLUMN));
        user.setName(rs.getString(NAME_COLUMN));
        if (isSetPassword) {
            user.setPassword(rs.getString(PASSWORD_COLUMN));
        }
        user.setRole(rs.getInt(ROLE_COLUMN));
        user.setEmail(rs.getString(EMAIL_COLUMN));
        return user;
    }

    private void removeAllUserContracts(Connection conn, int userId) throws SQLException {
        final PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_ALL_USER_CONTRACTS_QUERY);
        preparedStatement.setInt(1, userId);
        preparedStatement.execute();
    }

    private void removeAllUserOffers(Connection conn, int offerOwnerId) throws SQLException {
        final PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_ALL_USER_OFFERS_QUERY);
        preparedStatement.setInt(1, offerOwnerId);
        preparedStatement.execute();
    }

    private void removeAllOffersForUserContract(Connection conn, int offerOwnerId) throws SQLException {
        final PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_ALL_OFFERS_FOR_USER_CONTRACT_QUERY);
        preparedStatement.setInt(1, offerOwnerId);
        preparedStatement.execute();
    }

    private void removeAllUserCertificates(Connection conn, int offerOwnerId) throws SQLException {
        final PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_ALL_USER_CERTIFICATES_QUERY);
        preparedStatement.setInt(1, offerOwnerId);
        preparedStatement.execute();
    }
}
