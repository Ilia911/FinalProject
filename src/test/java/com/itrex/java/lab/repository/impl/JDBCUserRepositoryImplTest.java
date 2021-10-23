package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JDBCUserRepositoryImplTest extends BaseRepositoryTest {

    private final UserRepository repository;

    public JDBCUserRepositoryImplTest() {
        super();
        repository = new JDBCUserRepositoryImpl(getConnectionPool());
    }

    @Test
    public void find_validData_shouldReturnExistUser() throws RepositoryException {
        //given
        User expectedUser = new User(1, "Customer", "password", new Role(2, "customer"), "castomer@gmail.com", null);
        //when
        Optional<User> actualUser = repository.findByEmail("castomer@gmail.com");
        //then
        assertEquals(expectedUser, actualUser.get());
    }

    @Test
    public void find_null_shouldThrowRepositoryException() throws RepositoryException {

        //given && when
        String nullableEmail = null;
        //then
        assertThrows(RepositoryException.class, () -> repository.findByEmail(nullableEmail));
    }

    @Test
    public void find_nonExistsUserId_shouldReturnExistUser() throws RepositoryException {
        //given && when
        Optional<User> actualUser = repository.findByEmail("noneExist@gmail.com");
        //then
        assertTrue(actualUser.isEmpty());
    }

    @Test
    public void findAll_validData_shouldReturnExistUsers() throws RepositoryException {
        //given
        User expectedUser1 = new User(1, "Customer", null, new Role(2, "customer"), "castomer@gmail.com", null);
        User expectedUser2 = new User(2, "SecondCustomer", null, new Role(2, "customer"), "secondCastomer@gmail.com", null);
        User expectedUser3 = new User(3, "Contractor", null, new Role(3, "contractor"), "contractor@gmail.com", null);
        User expectedUser4 = new User(4, "SecondContractor", null, new Role(3, "contractor"), "SecondContractor@gmail.com", null);
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
        User expectedUser = new User(1, "updatedName", "updatedPassword", new Role(3, "contractor"), "updatedEmail@gmail.com", null);
        //when
        User actualUser = repository.update(expectedUser);
        //then
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void update_nonExistsUser_shouldReturnNull() throws RepositoryException {
        //given
        User expectedUser = new User(5, "updatedName", "updatedPassword", new Role(3, "contractor"), "updatedEmail@gmail.com", null);
        //when
        User actualUser = repository.update(expectedUser);
        //then
        assertNull(actualUser);
    }

    @Test
    void update_null_shouldThrowRepositoryException() throws RepositoryException {
        //given && when
        User nullUser = null;
        //then
        assertThrows(RepositoryException.class, () -> repository.update(nullUser));
    }

    @Test
    void add_validDate_shouldReturnNewUser() throws RepositoryException {
        //given
        User expectedUser = new User(5, "newUser", "password", new Role(2, "customer"), "newUserEmail.@gmail.com", null);
        //when
        Optional<User> actualUser = repository.add(expectedUser);
        //then
        assertEquals(expectedUser, actualUser.get());
    }

    @Test
    void add_userWithNotUniqueEmail_shouldThrowRepositoryException() throws RepositoryException {
        //given && when
        User userWithNotUniqueEmail = new User(5, "newUser", "password", new Role(2, "customer"), "castomer@gmail.com", null);
        //then
        assertThrows(RepositoryException.class, () -> repository.add(userWithNotUniqueEmail));
    }
}