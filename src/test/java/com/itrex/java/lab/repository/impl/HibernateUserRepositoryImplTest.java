package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.BaseRepositoryTest;
import com.itrex.java.lab.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HibernateUserRepositoryImplTest extends BaseRepositoryTest {

    private final UserRepository repository;

    public HibernateUserRepositoryImplTest() {
        super();
        repository = new HibernateUserRepositoryImpl(getSessionFactory().openSession());
    }

    @Test
    public void find_validData_shouldReturnExistUser() throws RepositoryException {
        //given
        User expectedUser = new User(1, "Customer", "password", new Role(2, "customer"), "castomer@gmail.com", new ArrayList<>());
        //when
        Optional<User> actualUser = repository.findByEmail("castomer@gmail.com");
        //then
        assertEquals(expectedUser, actualUser.get());
    }

    @Test
    public void findAll_validData_shouldReturnExistUsers() throws RepositoryException {
        //given
        List<Certificate> user3Certificates = new ArrayList<>();
        user3Certificates.add(new Certificate(1, "Filling window and door openings"));
        user3Certificates.add(new Certificate(6, "Execution of work on the arrangement of foundations, foundations of buildings and structures"));
        user3Certificates.add(new Certificate(7, "Performing work on the installation of thermal insulation of the enclosing structures of buildings and structures"));
        List<Certificate> user4Certificates = new ArrayList<>();
        user4Certificates.add(new Certificate(4, "Execution of works on the arrangement of road surfaces of pedestrian zones from sidewalk slabs"));
        user4Certificates.add(new Certificate(5, "Execution of works on the construction of insulating coatings"));
        User expectedUser1 = new User(1, "Customer", "password", new Role(2, "customer"), "castomer@gmail.com", new ArrayList<>());
        User expectedUser2 = new User(2, "SecondCustomer", "password", new Role(2, "customer"), "secondCastomer@gmail.com", new ArrayList<>());
        User expectedUser3 = new User(3, "Contractor", "password", new Role(3, "contractor"), "contractor@gmail.com", user3Certificates);
        User expectedUser4 = new User(4, "SecondContractor", "password", new Role(3, "contractor"), "SecondContractor@gmail.com", user4Certificates);
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
        //given && when
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
        User expectedUser = new User(1, "updatedName", "updatedPassword", new Role(3, "contractor"), "updatedEmail@gmail.com", new ArrayList<>());
        //when
        User actualUser = repository.update(expectedUser);
        //then
        assertEquals(expectedUser, actualUser);
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
}