package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRepositoryImplTest extends BaseRepositoryTest {

    private final UserRepository repository;

    public UserRepositoryImplTest() {
        super();
        repository = new UserRepositoryImpl(getConnectionPool());
    }

    @Test
    public void find_validData_shouldReturnExistUser() {
        User expectedUser = new User(1, "Customer", "password", 2, "castomer@gmail.com");

        Optional<User> actualUser = repository.find("castomer@gmail.com");

        assertEquals(expectedUser, actualUser.get());
    }

    @Test
    public void findAll_validData_shouldReturnExistUsers() {
        User expectedUser1 = new User(1, "Customer", null, 2, "castomer@gmail.com");
        User expectedUser2 = new User(2, "SecondCustomer", null, 2, "secondCastomer@gmail.com");
        User expectedUser3 = new User(3, "Contractor", null, 3, "contractor@gmail.com");
        User expectedUser4 = new User(4, "SecondContractor", null, 3, "SecondContractor@gmail.com");
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(expectedUser1);
        expectedUsers.add(expectedUser2);
        expectedUsers.add(expectedUser3);
        expectedUsers.add(expectedUser4);

        final List<User> actualUsers = repository.findAll();

        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void delete_validData_shouldDeleteUser() {

        assertTrue(repository.delete(1));
    }

    @Test
    void delete_invalidData_shouldDeleteUser() {

        assertFalse(repository.delete(5));
    }

    @Test
    void update() {
        User expectedUser = new User(1, "updatedName", "updatedPassword", 3, "updatedEmail@gmail.com");

        User actualUser = repository.update(expectedUser);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void add() {
        final User expectedUser = new User(5, "newUser", "password", 2, "newUserEmail.@gmail.com");

        final Optional<User> actualUser = repository.add(expectedUser);

        assertEquals(expectedUser, actualUser.get());
    }
}