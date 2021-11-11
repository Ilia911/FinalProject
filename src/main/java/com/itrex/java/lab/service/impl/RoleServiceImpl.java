package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.dto.RoleDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.RoleRepository;
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
    public Optional<RoleDTO> find(int id) throws ServiceException {
        Optional<RoleDTO> roleDTO;
        try {
            Optional<Role> role = repository.find(id);
            roleDTO = role.map(this::convertRoleIntoDTO);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return roleDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll() throws ServiceException {
        try {
            return repository.findAll().stream()
                    .map(this::convertRoleIntoDTO)
                    .collect(Collectors.toList());
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    private RoleDTO convertRoleIntoDTO(Role role) {
        return mapper.map(role, RoleDTO.class);
    }
}
