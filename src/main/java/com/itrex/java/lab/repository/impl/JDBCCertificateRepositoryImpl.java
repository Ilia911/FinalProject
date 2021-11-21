package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.CertificateRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCCertificateRepositoryImpl extends JdbcDaoSupport implements CertificateRepository {

    private static final String CERTIFICATE_ID_COLUMN = "id";
    private static final String CERTIFICATE_NAME_COLUMN = "name";
    private static final String FIND_ALL_CERTIFICATES_FOR_USER_QUERY
            = "select c.id, c.name from builder.user_certificate ulc " +
            "join builder.certificate c on ulc.certificate_id = c.id where user_id = ?";
    private static final String FIND_CERTIFICATE_BY_ID_QUERY
            = "select * from builder.certificate where id = ?";

    public JDBCCertificateRepositoryImpl(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    @Override
    public List<Certificate> findAllForUser(int userId) throws RepositoryException {
        try (Connection conn = getDataSource().getConnection()) {
            List<Certificate> certificateList = new ArrayList<>();
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL_CERTIFICATES_FOR_USER_QUERY);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                certificateList.add(createCertificate(resultSet));
            }
            return certificateList;
        } catch (SQLException ex) {
            throw new RepositoryException("Can't find certificates", ex);
        }
    }

    @Override
    public Optional<Certificate> findById(int id) throws RepositoryException {
        Certificate certificate = null;
        try (Connection conn = getDataSource().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_CERTIFICATE_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                certificate = createCertificate(resultSet);
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Can't find certificates", ex);
        }
        return Optional.ofNullable(certificate);
    }

    private Certificate createCertificate(ResultSet resultSet) throws SQLException {
        return Certificate.builder()
                .id(resultSet.getInt(CERTIFICATE_ID_COLUMN))
                .name(resultSet.getString(CERTIFICATE_NAME_COLUMN))
                .build();
    }
}
