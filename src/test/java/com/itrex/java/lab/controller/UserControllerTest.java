package com.itrex.java.lab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.entity.dto.RoleDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.service.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    void findById_validData_shouldReturnUser() throws Exception {
        //given
        int userId = 1;
        User user = User.builder().id(userId).name("Customer").email("castomer@gmail.com").build();
        UserDTO expectedResponseBody = UserDTO.builder().id(userId).name("Customer").email("castomer@gmail.com").build();
        // when
        when(service.findById(userId)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(get("/user/{id}", userId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    void findByEmail_validData_shouldReturnUser() throws Exception {
        //given
        int userId = 1;
        String userName = "Customer";
        String userPassword = "password";
        String userEmail = "castomer@gmail.com";
        UserDTO expectedResponseBody = UserDTO.builder().id(userId).name(userName).password(userPassword).email(userEmail).build();
        // when
        when(service.findByEmail(userEmail)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(get("/user")
                        .contentType("application/json")
                        .param("email", userEmail))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    void findAll_validData_shouldReturnUserList() throws Exception {
        //given
        UserDTO userDTO = UserDTO.builder().build();
        // when
        List<UserDTO> expectedResponseBody = Arrays.asList(userDTO, userDTO, userDTO, userDTO);
        when(service.findAll()).thenReturn(expectedResponseBody);
        //then
        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    void delete_validUserWithContract_shouldReturnTrue() throws Exception {
        //given
        int id = 1;
        // when
        when(service.delete(id)).thenReturn(true);
        //then
        mockMvc.perform(delete("/user/delete/{id}", id)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_invalidData_shouldReturnFalse() throws Exception {
        //given && when
        int invalidId = 5;
        when(service.delete(invalidId)).thenReturn(false);
        //then
        mockMvc.perform(delete("/user/delete/{id}", invalidId)
                        .contentType("application/json"))
                .andExpect(status().isNotModified());
    }

    @Test
    void update_validData_shouldReturnUpdatedUserDTO() throws Exception {
        //given
        int userId = 1;
        String name = "updatedName";
        RoleDTO roleDTO = RoleDTO.builder().id(3).name("contractor").build();
        UserDTO expectedResponseBody = UserDTO.builder().id(userId).name(name).password("pass").role(roleDTO).email("email").build();
        //when
        when(service.update(expectedResponseBody)).thenReturn(expectedResponseBody);
        // then
        MvcResult mvcResult = mockMvc.perform(put("/user/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponseBody)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    void add_validData_shouldReturnNewUserDTO() throws Exception {
        //given
        UserDTO expectedResponseBody = UserDTO.builder().id(5).name("newUser").password("pass").role(RoleDTO.builder().id(2).name("customer").build()).email("email").build();
        //when
        when(service.add(expectedResponseBody)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(post("/user/new")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponseBody)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    void assignCertificate_validDate_shouldReturnCertificateList() throws Exception {
        //given
        int userId = 3;
        int certificateId = 2;
        CertificateDTO certificateDTO = CertificateDTO.builder().build();
        List<CertificateDTO> expectedResponseBody = Arrays.asList(certificateDTO, certificateDTO, certificateDTO);
        //when
        when(service.assignCertificate(userId, certificateId))
                .thenReturn(expectedResponseBody);
        //then
        MvcResult mvcResult = mockMvc.perform(post("/user/assignCertificate")
                        .contentType("application/json")
                        .param("userId", String.valueOf(userId))
                        .param("certificateId", String.valueOf(certificateId)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);

    }

    @Test
    void removeCertificate_validData_shouldDeleteUserCertificate() throws Exception {
        //given
        int userId = 3;
        int certificateId = 1;
        CertificateDTO certificateDTO = CertificateDTO.builder().build();
        List<CertificateDTO> expectedResponseBody = Arrays.asList(certificateDTO);
        //when
        when(service.removeCertificate(userId, certificateId))
                .thenReturn(expectedResponseBody);
        //then
        MvcResult mvcResult = mockMvc.perform(post("/user/removeCertificate")
                        .contentType("application/json")
                        .param("userId", String.valueOf(userId))
                        .param("certificateId", String.valueOf(certificateId)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }
}