package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.repository.JDBCUserListCertificateRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

public class JDBCUserListCertificateRepositoryImpl implements JDBCUserListCertificateRepository {

    private final DataSource dataSource;

    private static final String USER_ID_COLUMN = "id";
    private static final String CERTIFICATE_ID_COLUMN = "id";
    private static final String CERTIFICATE_NAME_COLUMN = "name";
    private static final String FIND_CERTIFICATE_BY_USER_ID_AND_CERTIFICATE_ID_QUERY
            = "select c.id, c.name from builder.user_list_certificate ulc " +
            "join builder.certificate c on ulc.certificate_id = c.id where user_id = ? and certificate_id = ?";
    private static final String ASSIGN_CERTIFICATE_BY_USER_ID_QUERY
            = "insert into builder.user_list_certificate (user_id, certificate_id) values (?, ?);";
    private static final String REMOVE_CERTIFICATE_BY_USER_ID_QUERY
            = "delete from builder.user_list_certificate where user_id = ? and certificate_id = ?;";
    private static final String FIND_ALL_CERTIFICATES_FOR_USER_QUERY
            = "select c.id, c.name from builder.user_list_certificate ulc " +
            "join builder.certificate c on ulc.certificate_id = c.id where user_id = ?";

    public JDBCUserListCertificateRepositoryImpl(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Certificate> assignCertificate(int userId, int certificateId) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement
                    = conn.prepareStatement(ASSIGN_CERTIFICATE_BY_USER_ID_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, certificateId);

            int effectiveRaws = preparedStatement.executeUpdate();

            if (effectiveRaws == 1) {

                PreparedStatement prepareStatementForCreatedCertificate
                        = conn.prepareStatement(FIND_CERTIFICATE_BY_USER_ID_AND_CERTIFICATE_ID_QUERY);
                prepareStatementForCreatedCertificate.setInt(1, userId);
                prepareStatementForCreatedCertificate.setInt(2, certificateId);
                ResultSet resultSet = prepareStatementForCreatedCertificate.executeQuery();

                if (resultSet.next()) {
                    Certificate certificate = new Certificate(resultSet.getInt(USER_ID_COLUMN),
                            resultSet.getString(CERTIFICATE_NAME_COLUMN));
                    return Optional.of(certificate);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean removeCertificate(int userId, int certificateId) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_CERTIFICATE_BY_USER_ID_QUERY);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, certificateId);
            return 1 == preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Certificate> findAllForUser(int userId) {
        List<Certificate> certificateList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL_CERTIFICATES_FOR_USER_QUERY);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Certificate certificate = new Certificate(resultSet.getInt(CERTIFICATE_ID_COLUMN),
                        resultSet.getString(CERTIFICATE_NAME_COLUMN));
                certificateList.add(certificate);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return certificateList;
    }
}
