package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.JDBCUserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JDBCUserRepositoryImplTest extends BaseRepositoryTest {

    private final JDBCUserRepository repository;

    public JDBCUserRepositoryImplTest() {
        super();
        repository = new JDBCUserRepositoryImpl(getConnectionPool());
    }

    @Test
    public void find_validData_shouldReturnExistUser() throws RepositoryException {
        //given
        User expectedUser = new User(1, "Customer", "password", 2, "castomer@gmail.com", null);
        //when
        Optional<User> actualUser = repository.find("castomer@gmail.com");
        //then
        assertEquals(expectedUser, actualUser.get());
    }

    @Test
    public void findAll_validData_shouldReturnExistUsers() throws RepositoryException {
        //given
        User expectedUser1 = new User(1, "Customer", null, 2, "castomer@gmail.com", null);
        User expectedUser2 = new User(2, "SecondCustomer", null, 2, "secondCastomer@gmail.com", null);
        User expectedUser3 = new User(3, "Contractor", null, 3, "contractor@gmail.com", null);
        User expectedUser4 = new User(4, "SecondContractor", null, 3, "SecondContractor@gmail.com", null);
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(expectedUser1);
        expectedUsers.add(expectedUser2);
        expectedUsers.add(expectedUser3);
        expectedUsers.add(expectedUser4);
        //when
        List<User> actualUsers = repository.findAll();
        //then
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void delete_validData_shouldDeleteUser() throws RepositoryException {
        //when
        int userId = 1;
        //then
        assertTrue(repository.delete(userId));
    }

    @Test
    void delete_invalidData_shouldDeleteUser() throws RepositoryException {
        //when
        int userId = 5;
        //then
        assertFalse(repository.delete(userId));
    }

    @Test
    void update_validData_shouldReturnUpdatedUser() throws RepositoryException {
        //given
        User expectedUser = new User(1, "updatedName", "updatedPassword", 3, "updatedEmail@gmail.com", null);
        //when
        User actualUser = repository.update(expectedUser);
        //then
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void add_validDate_shouldReturnNewUser() throws RepositoryException {
        //given
        User expectedUser = new User(5, "newUser", "password", 2, "newUserEmail.@gmail.com", null);
        //when
        Optional<User> actualUser = repository.add(expectedUser);
        //then
        assertEquals(expectedUser, actualUser.get());
    }
}