package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Primary
public class HibernateRoleRepositoryImpl implements RoleRepository {

    private static final String FIND_ROLES_QUERY = "select r from Role r";

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Role> find(int id) throws RepositoryException {
        try {
            Session session = sessionFactory.getCurrentSession();
            Role role = session.find(Role.class, id);
            return Optional.ofNullable(role);
        } catch (Exception ex) {
            throw new RepositoryException("Can not find 'role'");
        }
    }

    @Override
    public List<Role> findAll() throws RepositoryException {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery(FIND_ROLES_QUERY, Role.class).getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("Can not find 'roles'");
        }
    }
}
