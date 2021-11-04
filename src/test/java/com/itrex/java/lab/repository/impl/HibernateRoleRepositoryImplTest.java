package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernateRoleRepositoryImplTest extends BaseRepositoryTest {

    @Qualifier("hibernateRoleRepositoryImpl")
    @Autowired
    private RoleRepository repository;

    HibernateRoleRepositoryImplTest() {
        super();
    }

    @Test
    void find_validData_shouldReturnRole() throws RepositoryException {
        //given
        int expectedRoleId = 2;
        String expectedRoleName = "customer";
        // when
        Role actualRole = repository.find(expectedRoleId).get();
        // then
        assertEquals(expectedRoleId, actualRole.getId());
        assertEquals(expectedRoleName, actualRole.getName());
    }

    @Test
    void find_invalidData_shouldEmptyOptional() throws RepositoryException {
        //given && when
        int roleId = -2;
        Optional<Role> actualOptionalRole = repository.find(roleId);
        // then
        assertTrue(actualOptionalRole.isEmpty());
    }

    @Test
    void findAll_validData_shouldReturnRoleList() throws RepositoryException {
        //given
        int expectedListSize = 2;
        //when
        List<Role> actualRoles = repository.findAll();
        //then
        assertEquals(expectedListSize, actualRoles.size());
    }
}