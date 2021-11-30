package com.itrex.java.lab.controller;

import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {

        Page<UserDTO> users = service.findAll(pageable);

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable(name = "id") int id) {

        Optional<UserDTO> user = service.findById(id);

        return user.map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/email")
    public ResponseEntity<UserDTO> findByEmail(@RequestParam(name = "email") String email) {

        Optional<UserDTO> user = service.findByEmail(email);

        return user.map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") int id) {

        boolean result = service.delete(id);

        return result ? new ResponseEntity(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable(name = "id") int id,
                                          @RequestBody UserDTO userDTO) throws ServiceException {

        UserDTO updatedContract = service.update(userDTO);

        return updatedContract != null
                ? new ResponseEntity<>(updatedContract, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping
    public ResponseEntity<UserDTO> add(@RequestBody UserDTO userDTO) throws ServiceException {

        Optional<UserDTO> newUser = service.add(userDTO);

        return newUser.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK)).
                orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
    }

    @PostMapping("/assignCertificate")
    public ResponseEntity<List<CertificateDTO>>
    assignCertificate(@RequestParam(name = "userId") int userId, @RequestParam(name = "certificateId") int certificateId) {

        List<CertificateDTO> certificateDTOS = service.assignCertificate(userId, certificateId);

        return certificateDTOS != null
                ? new ResponseEntity<>(certificateDTOS, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/removeCertificate")
    public ResponseEntity<List<CertificateDTO>>
    removeCertificate(@RequestParam(name = "userId") int userId, @RequestParam(name = "certificateId") int certificateId) {

        List<CertificateDTO> certificateDTOS = service.removeCertificate(userId, certificateId);

        return certificateDTOS != null
                ? new ResponseEntity<>(certificateDTOS, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
