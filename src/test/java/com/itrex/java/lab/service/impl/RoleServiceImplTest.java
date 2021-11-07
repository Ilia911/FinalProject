package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.service.RoleService;
import com.itrex.java.lab.service.TestServiceConfiguration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
class RoleServiceImplTest {

    @Autowired
    private RoleService service;
    @Autowired
    private RoleRepository repository;

    @Test
    void find_validData_shouldReturnRole() throws RepositoryException, ServiceException {
        //given
        int expectedRoleId = 2;
        String expectedRoleName = "customer";
        // when
        Mockito.when(repository.find(expectedRoleId)).thenReturn(Optional.of(new Role(2, expectedRoleName)));
        Role actualRole = service.find(expectedRoleId).get();
        // then
        assertEquals(expectedRoleId, actualRole.getId());
        assertEquals(expectedRoleName, actualRole.getName());
    }

    @Test
    void find_invalidData_shouldReturnEmptyOptional() throws RepositoryException {
        //given
        int roleId = -2;
        //when
        Mockito.when(repository.find(roleId)).thenReturn(Optional.empty());
        Optional<Role> actualOptionalRole = repository.find(roleId);

        // then
        assertTrue(actualOptionalRole.isEmpty());
    }

    @Test
    void findAll_validData_shouldReturnRoleList() throws RepositoryException {
        //given
        int expectedListSize = 2;
        //when
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(new Role(), new Role()));
        List<Role> actualRoles = repository.findAll();
        //then
        assertEquals(expectedListSize, actualRoles.size());
    }
}