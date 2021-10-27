package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.RoleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JDBCRoleRepositoryImplTest extends BaseRepositoryTest {

    private final RoleRepository repository;

    JDBCRoleRepositoryImplTest() {
        this.repository = new JDBCRoleRepositoryImpl(getConnectionPool());
    }

    @Test
    void find_validData_shouldReturnRole() throws RepositoryException {
        //given
        int roleId = 2;
        Role expectedRole = new Role(2, "customer");
        // when
        Role actualRole = repository.find(roleId).get();
        // then
        assertRoleEquals(expectedRole, actualRole);
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
        List<Role> expectedRoles = new ArrayList<>();
        Role expectedRole1 = new Role(2, "customer");
        Role expectedRole2 = new Role(3, "contractor");
        expectedRoles.add(expectedRole1);
        expectedRoles.add(expectedRole2);
        //when
        final List<Role> actualRoles = repository.findAll();
        //then
        assertEquals(expectedRoles.size(), actualRoles.size());
        for (int i = 1; i < expectedRoles.size(); i++) {
            assertRoleEquals(expectedRoles.get(i), actualRoles.get(i));
        }
    }

    private void assertRoleEquals(Role expectedRole, Role actualRole) {
        assertEquals(expectedRole.getId(), actualRole.getId());
        assertEquals(expectedRole.getName(), actualRole.getName());
    }
}