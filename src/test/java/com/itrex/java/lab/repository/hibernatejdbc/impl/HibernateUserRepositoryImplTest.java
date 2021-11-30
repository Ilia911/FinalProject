package com.itrex.java.lab.repository.hibernatejdbc.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.hibernatejdbc.UserRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
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
    public void findById_validData_shouldReturnExistUser() throws RepositoryException {
        //given
        int userId = 1;
        User expectedUser = User.builder().id(userId).name("Customer").email("castomer@gmail.com").build();
        //when
        User actualUser = repository.findById(userId).get();
        assertUserEquals(expectedUser, actualUser);

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
    void delete_invalidData_shouldDeleteUser() throws RepositoryException {
        //given && when
        int userId = 5;
        //then
        assertFalse(repository.delete(userId));
    }

    @Test
    void update_validData_shouldReturnUpdatedUser() throws RepositoryException {
        //given
        User expectedUser = User.builder().id(1).name("updatedName").password("updatedPassword")
                .role(Role.builder().id(3).name("contractor").build()).email("updatedEmail@gmail.com")
                .certificates(new ArrayList<>()).build();
        //when
        User actualUser = repository.update(expectedUser);
        //then
        assertUserEquals(expectedUser, actualUser);
    }

    @Test
    void add_validDate_shouldReturnNewUser() throws RepositoryException {
        //given
        User expectedUser = User.builder().id(5).name("updatedName").password("updatedPassword")
                .role(Role.builder().id(3).name("contractor").build()).email("updatedEmail@gmail.com")
                .certificates(new ArrayList<>()).build();
        //when
        Optional<User> actualUser = repository.add(expectedUser);
        //then
        assertUserEquals(expectedUser, actualUser.get());
    }

    private void assertUserEquals(User expectedUser, User actualUser) {
        assertAll(
                () -> assertEquals(expectedUser.getId(), actualUser.getId()),
                () -> assertEquals(expectedUser.getName(), actualUser.getName()),
                () -> assertEquals(expectedUser.getEmail(), actualUser.getEmail())
        );
    }
}