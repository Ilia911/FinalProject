package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.dto.RoleDTO;
import com.itrex.java.lab.repository.data.RoleRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @InjectMocks
    private RoleServiceImpl service;
    @Mock
    private RoleRepository repository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void find_validData_shouldReturnRole() {
        //given
        int id = 2;
        String name = "customer";
        Role role = Role.builder().id(id).name(name).build();
        RoleDTO roleDTO = RoleDTO.builder().id(id).name(name).build();
        // when
        when(repository.findById(id)).thenReturn(Optional.of(role));
        when(modelMapper.map(role, RoleDTO.class)).thenReturn(roleDTO);
        RoleDTO actualRole = service.find(id).get();
        // then
        assertEquals(id, actualRole.getId());
        assertEquals(name, actualRole.getName());
    }

    @Test
    void find_invalidData_shouldReturnEmptyOptional() {
        //given
        int roleId = -2;
        //when
        when(repository.findById(roleId)).thenReturn(Optional.empty());
        Optional<RoleDTO> actualOptionalRole = service.find(roleId);
        // then
        assertTrue(actualOptionalRole.isEmpty());
    }

    @Test
    void findAll_validData_shouldReturnRoleList() {
        //given
        int expectedListSize = 2;
        Role role = Role.builder().build();
        RoleDTO roleDTO = RoleDTO.builder().build();
        //when
        when(modelMapper.map(role, RoleDTO.class)).thenReturn(roleDTO);
        when(repository.findAll()).thenReturn(Arrays.asList(role, role));
        List<RoleDTO> actualRoles = service.findAll();
        //then
        assertEquals(expectedListSize, actualRoles.size());
    }
}