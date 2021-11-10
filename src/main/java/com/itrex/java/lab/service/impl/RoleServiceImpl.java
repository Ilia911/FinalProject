package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.service.RoleService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> find(int id) throws ServiceException {
        Optional<Role> role;
        try {
            role = repository.find(id);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return role;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() throws ServiceException {
        List<Role> roles;
        try {
            roles = repository.findAll();
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return roles;
    }
}
