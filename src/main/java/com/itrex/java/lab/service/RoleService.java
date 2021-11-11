package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.dto.RoleDTO;
import com.itrex.java.lab.exeption.ServiceException;
import java.util.List;
import java.util.Optional;

public interface RoleService {

    Optional<RoleDTO> find(int id) throws ServiceException;

    List<RoleDTO> findAll() throws ServiceException;
}
