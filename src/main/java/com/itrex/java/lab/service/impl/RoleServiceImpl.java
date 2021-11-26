package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.dto.RoleDTO;
import com.itrex.java.lab.repository.data.RoleRepository;
import com.itrex.java.lab.service.RoleService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final ModelMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDTO> find(int id) {

        Optional<Role> role = repository.findById(id);
        return role.map(this::convertRoleIntoDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll() {

        return repository.findAll().stream()
                .map(this::convertRoleIntoDTO)
                .collect(Collectors.toList());
    }

    private RoleDTO convertRoleIntoDTO(Role role) {
        return mapper.map(role, RoleDTO.class);
    }
}
