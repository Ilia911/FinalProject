package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.TestRepositoryConfiguration;
import com.itrex.java.lab.repository.UserRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
class HibernateUserRepositoryImplTest extends BaseRepositoryTest {

    @Qualifier("hibernateUserRepositoryImpl")
    @Autowired
    private UserRepository repository;

    @Test
    public void findByEmail_validData_shouldReturnExistUser() throws RepositoryException {
        //given
        String userName = "Customer";
        String userPassword = "password";
        String userEmail = "castomer@gmail.com";
        //when
        User actualUser = repository.findByEmail("castomer@gmail.com").get();
        //then
        assertEquals(userName, actualUser.getName());
        assertEquals(userPassword, actualUser.getPassword());
        assertEquals(userEmail, actualUser.getEmail());
    }

    @Test
    public void findAll_validData_shouldReturnExistUsers() throws RepositoryException {
        //given
        int expectedUsersAmount = 4;
        //when
        int actualUsersAmount = repository.findAll().size();
        //then
        assertEquals(expectedUsersAmount, actualUsersAmount);
    }

    @Test
    void delete_validData_shouldDeleteUser() throws RepositoryException {
        //given && when
        int userId = 1;
        //then
        assertTrue(repository.delete(userId));
    }

    @Test
    void delete_invalidData_shouldReturnFalse() throws RepositoryException {
        //given && when
        int userId = 5;
        //then
        assertFalse(repository.delete(userId));
    }

    @Test
    void update_validData_shouldReturnUpdatedUser() throws RepositoryException {
        //given
        User expectedUser = new User(1, "updatedName", "updatedPassword", new Role(3, "contractor"), "updatedEmail@gmail.com", new ArrayList<>());
        //when
        User actualUser = repository.update(expectedUser);
        //then
        assertUserEquals(expectedUser, actualUser);
    }

    @Test
    void add_validDate_shouldReturnNewUser() throws RepositoryException {
        //given
        User expectedUser = new User(5, "newUser", "password", new Role(2, "customer"), "newUserEmail.@gmail.com", null);
        //when
        Optional<User> actualUser = repository.add(expectedUser);
        //then
        assertUserEquals(expectedUser, actualUser.get());
    }

    private void assertUserEquals(User expectedUser, User actualUser) {
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
    }
}