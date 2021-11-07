package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.TestServiceConfiguration;
import com.itrex.java.lab.service.UserService;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
class UserServiceImplTest {

    @Autowired
    private UserService service;
    @Autowired
    private UserRepository repository;

    @Test
    void findAll_validData_shouldReturnUserList() throws ServiceException, RepositoryException {
        //given
        int expectedSize = 4;
        // when
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(new User(), new User(), new User(), new User()));
        int actualSize = service.findAll().size();
        //then
        assertEquals(expectedSize, actualSize);
    }

    @Test
    void delete_validData_shouldReturnTrue() throws RepositoryException, ServiceException {
        //given && when
        int validId = 1;
        Mockito.when(repository.delete(validId)).thenReturn(true);
        //then
        assertTrue(service.delete(validId));
    }

    @Test
    void delete_invalidData_shouldReturnFalse() throws RepositoryException, ServiceException {
        //given && when
        int invalidId = 5;
        Mockito.when(repository.delete(invalidId)).thenReturn(false);
        //then
        assertFalse(service.delete(invalidId));
    }

    @Test
    void update_validData_shouldReturnUpdatedUserDTO() throws RepositoryException, ServiceException {
        //given
        int userId = 1;
        String name = "updatedName";
        Role role = new Role(3, "contractor");
        User user = new User(userId, name, "pass", role, "email", null);
        //when
        Mockito.when(repository.update(user)).thenReturn(user);
        UserDTO actualUpdatedUser = service.update(user);
        // then
        assertAll(() -> assertEquals(userId, actualUpdatedUser.getId()),
                () -> assertEquals(name, actualUpdatedUser.getName()),
                () -> assertEquals(role.getId(), actualUpdatedUser.getRole().getId())
        );
    }

    @Test
    void register_validData_shouldReturnNewUserDTO() throws RepositoryException, ServiceException {
        //given
        User user = new User(5, "newUser", "pass", new Role(2, "customer"), "email", null);
        //when
        Mockito.when(repository.add(user)).thenReturn(Optional.of(user));
        UserDTO actualUserDTO = service.register(user).get();
        //then
        assertAll(() -> assertEquals(user.getId(), actualUserDTO.getId()),
                () -> assertEquals(user.getName(), actualUserDTO.getName()),
                () -> assertEquals(user.getRole().getId(), actualUserDTO.getRole().getId()));
    }

    @Test
    void login_validData_shouldReturnUserDTO() throws RepositoryException, ServiceException {
        //given
        String email = "castomer@gmail.com";
        String password = "password";
        int userId = 1;
        String name = "customer";
        int roleId = 2;
        User user = new User(userId, name, password, new Role(roleId, "customer"), email, null);
        //when
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDTO actualUserDTO = service.login(email, password).get();
        //then
        assertAll(
                () -> assertEquals(userId, actualUserDTO.getId()),
                () -> assertEquals(name, actualUserDTO.getName()),
                () -> assertEquals(roleId, actualUserDTO.getRole().getId())
        );
    }
}