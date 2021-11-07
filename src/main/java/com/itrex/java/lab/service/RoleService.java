package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exeption.ServiceException;
import java.util.List;
import java.util.Optional;

public interface RoleService {

    Optional<Role> find(int id) throws ServiceException;

    List<Role> findAll() throws ServiceException;
}
