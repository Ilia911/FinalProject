package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exeption.RepositoryException;
import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Optional<Role> find(int id) throws RepositoryException;

    List<Role> findAll() throws RepositoryException;
}
