package com.itrex.java.lab.controller;

import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> findAll() throws ServiceException {

        List<UserDTO> users = service.findAll();

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable(name = "id") int id) throws ServiceException {

        Optional<UserDTO> user = service.findById(id);

        return user.isPresent()
                ? new ResponseEntity<>(user.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> findByEmail(@RequestParam(name = "email") String email) throws ServiceException {

        Optional<UserDTO> user = service.findByEmail(email);

        return user.isPresent()
                ? new ResponseEntity<>(user.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "id") int id) throws ServiceException {

        boolean result = service.delete(id);

        return result ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(result, HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("user/update")
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO userDTO) throws ServiceException {

        UserDTO updatedContract = service.update(userDTO);

        return updatedContract != null
                ? new ResponseEntity<>(updatedContract, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/user/new")
    public ResponseEntity<UserDTO> add(@RequestBody UserDTO userDTO) throws ServiceException {

        Optional<UserDTO> newUser = service.add(userDTO);

        return newUser.isPresent()
                ? new ResponseEntity<>(newUser.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/user/assignCertificate")
    public ResponseEntity<List<CertificateDTO>>
    assignCertificate(@RequestParam(name = "userId") int userId, @RequestParam(name = "certificateId") int certificateId)
            throws ServiceException {

        List<CertificateDTO> certificateDTOS = service.assignCertificate(userId, certificateId);

        return certificateDTOS != null
                ? new ResponseEntity<>(certificateDTOS, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/user/removeCertificate")
    public ResponseEntity<List<CertificateDTO>>
    removeCertificate(@RequestParam(name = "userId") int userId, @RequestParam(name = "certificateId") int certificateId)
            throws ServiceException {

        List<CertificateDTO> certificateDTOS = service.removeCertificate(userId, certificateId);

        return certificateDTOS != null
                ? new ResponseEntity<>(certificateDTOS, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
