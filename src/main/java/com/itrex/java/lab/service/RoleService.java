package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.dto.RoleDTO;
import java.util.List;
import java.util.Optional;

public interface RoleService {

    Optional<RoleDTO> find(int id);

    List<RoleDTO> findAll();
}
