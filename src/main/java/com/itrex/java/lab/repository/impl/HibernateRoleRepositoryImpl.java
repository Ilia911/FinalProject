package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;

public class HibernateRoleRepositoryImpl implements RoleRepository {

    private final Session session;
    private static final String FIND_ROLES_QUERY = "select r from Role r";

    public HibernateRoleRepositoryImpl(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Role> find(int id) throws RepositoryException {
        try {
            Role role = session.find(Role.class, id);
            return Optional.ofNullable(role);
        } catch (Exception ex) {
            throw new RepositoryException("Can not find 'role'");
        }
    }

    @Override
    public List<Role> findAll() throws RepositoryException {
        try {
            return session.createQuery(FIND_ROLES_QUERY, Role.class).getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("Can not find 'roles'");
        }
    }
}
