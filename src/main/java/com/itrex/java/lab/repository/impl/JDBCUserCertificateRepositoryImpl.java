package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.UserListCertificateRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

public class JDBCUserCertificateRepositoryImpl implements UserListCertificateRepository {

    private final DataSource dataSource;

    private static final String USER_ID_COLUMN = "id";
    private static final String CERTIFICATE_ID_COLUMN = "id";
    private static final String CERTIFICATE_NAME_COLUMN = "name";
    private static final String FIND_CERTIFICATE_BY_USER_ID_AND_CERTIFICATE_ID_QUERY
            = "select c.id, c.name from builder.user_certificate ulc " +
            "join builder.certificate c on ulc.certificate_id = c.id where user_id = ? and certificate_id = ?";
    private static final String ASSIGN_CERTIFICATE_BY_USER_ID_QUERY
            = "insert into builder.user_certificate (user_id, certificate_id) values (?, ?);";
    private static final String REMOVE_CERTIFICATE_BY_USER_ID_QUERY
            = "delete from builder.user_certificate where user_id = ? and certificate_id = ?;";
    private static final String FIND_ALL_CERTIFICATES_FOR_USER_QUERY
            = "select c.id, c.name from builder.user_certificate ulc " +
            "join builder.certificate c on ulc.certificate_id = c.id where user_id = ?";

    public JDBCUserCertificateRepositoryImpl(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Certificate> assignCertificate(int userId, int certificateId) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            Certificate certificate = null;
            try {
                conn.setAutoCommit(false);
                PreparedStatement preparedStatement
                        = conn.prepareStatement(ASSIGN_CERTIFICATE_BY_USER_ID_QUERY, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, certificateId);

                int effectiveRaws = preparedStatement.executeUpdate();
                if (effectiveRaws == 1) {
                    certificate = findCertificate(conn, userId, certificateId);
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new RepositoryException("Can't assign certificate", ex);
            } finally {
                conn.setAutoCommit(true);
            }
            return Optional.ofNullable(certificate);
        } catch (SQLException ex) {
            throw new RepositoryException("Can't assign certificate", ex);
        }
    }

    @Override
    public boolean removeCertificate(int userId, int certificateId) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_CERTIFICATE_BY_USER_ID_QUERY);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, certificateId);
            return 1 == preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RepositoryException("Can't remove certificate", ex);
        }
    }

    @Override
    public List<Certificate> findAllForUser(int userId) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            List<Certificate> certificateList = new ArrayList<>();
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL_CERTIFICATES_FOR_USER_QUERY);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Certificate certificate = new Certificate(resultSet.getInt(CERTIFICATE_ID_COLUMN),
                        resultSet.getString(CERTIFICATE_NAME_COLUMN));
                certificateList.add(certificate);
            }
            return certificateList;
        } catch (SQLException ex) {
            throw new RepositoryException("Can't find certificates", ex);
        }
    }

    private Certificate findCertificate(Connection conn, int userId, int certificateId) throws SQLException {
        PreparedStatement preparedStatement
                = conn.prepareStatement(FIND_CERTIFICATE_BY_USER_ID_AND_CERTIFICATE_ID_QUERY);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, certificateId);
        ResultSet resultSet = preparedStatement.executeQuery();

        Certificate certificate = null;

        if (resultSet.next()) {
            certificate = new Certificate(resultSet.getInt(USER_ID_COLUMN),
                    resultSet.getString(CERTIFICATE_NAME_COLUMN));
        }
        return certificate;
    }
}
