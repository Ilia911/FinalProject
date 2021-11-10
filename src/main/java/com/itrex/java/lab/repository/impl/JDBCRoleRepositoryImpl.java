package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.RoleRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JDBCRoleRepositoryImpl implements RoleRepository {

    private static final String FIND_ROLE_QUERY = "select * from builder.role where id = ?";
    private static final String FIND_ROLES_QUERY = "select * from builder.role";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";

    private final JdbcConnectionPool dataSource;

    @Override
    public Optional<Role> find(int id) throws RepositoryException {
        Role role = null;
        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement preparedStatement = conn.prepareStatement(FIND_ROLE_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                role = createRole(resultSet);
            }
            return Optional.ofNullable(role);
        } catch (SQLException ex) {
            throw new RepositoryException("Can't find Role", ex);
        }
    }

    @Override
    public List<Role> findAll() throws RepositoryException {
        List<Role> roles = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ROLES_QUERY);
            while (resultSet.next()) {
                roles.add(createRole(resultSet));
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Can't find roles", ex);
        }
        return roles;
    }

    private Role createRole(ResultSet resultSet) throws SQLException {
        Role role;
        role = new Role();
        role.setId(resultSet.getInt(ID_COLUMN));
        role.setName(resultSet.getString(NAME_COLUMN));
        return role;
    }
}
