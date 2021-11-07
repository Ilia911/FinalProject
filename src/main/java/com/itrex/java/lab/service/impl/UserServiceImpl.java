package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(@Qualifier("hibernateUserRepositoryImpl") UserRepository userRepository,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDTO> findAll() throws ServiceException {
        List<UserDTO> usersDTO;
        try {
            usersDTO = userRepository.findAll().stream().map(this::convertUserToUserDTO).collect(Collectors.toList());
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return usersDTO;
    }

    @Override
    public boolean delete(int id) throws ServiceException {

        try {
            return userRepository.delete(id);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    public UserDTO update(User user) throws ServiceException {
        UserDTO userDTO = null;
        try {
            User updatedUser = userRepository.update(user);
            userDTO = convertUserToUserDTO(updatedUser);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return userDTO;
    }

    @Override
    public Optional<UserDTO> register(User user) throws ServiceException {
        UserDTO createdUserDTO = null;
        try {
            Optional<User> createdUser = userRepository.add(user);
            if (createdUser.isPresent()) {
                createdUserDTO = convertUserToUserDTO(createdUser.get());
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return Optional.ofNullable(createdUserDTO);
    }

    @Override
    public Optional<UserDTO> login(String email, String password) throws ServiceException {

        UserDTO userDTO = null;
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
                userDTO = convertUserToUserDTO(optionalUser.get());
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return Optional.ofNullable(userDTO);
    }

    private UserDTO convertUserToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
