package com.itrex.java.lab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.entity.dto.RoleDTO;
import com.itrex.java.lab.entity.dto.UserDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "Unnecessary", roles = {"ADMIN", "CUSTOMER", "CONTRACTOR"})
    void findById_validData_shouldReturnUser() throws Exception {
        //given
        int userId = 1;
        UserDTO expectedResponseBody = UserDTO.builder().id(userId).name("Customer").email("castomer@gmail.com").build();
        // when
        when(userService.findById(userId)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(get("/users/{id}", userId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    void findByEmail_validData_shouldReturnUser() throws Exception {
        //given
        int userId = 1;
        String userName = "Customer";
        String userPassword = "password";
        String userEmail = "castomer@gmail.com";
        UserDTO expectedResponseBody = UserDTO.builder().id(userId).name(userName).password(userPassword).email(userEmail).build();
        // when
        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(get("/users/email")
                        .contentType("application/json")
                        .param("email", userEmail))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    @WithMockUser(username = "Unnecessary", roles = {"ADMIN", "CUSTOMER", "CONTRACTOR"})
    void findAll_validData_shouldReturnUserPage() throws Exception {
        //given
        UserDTO userDTO = UserDTO.builder().id(1).name("first").build();
        UserDTO userDTO2 = UserDTO.builder().id(2).name("second").build();
        Pageable pageable = PageRequest.of(1, 2, Sort.by("name").descending());
        // when
        Page<UserDTO> expectedResponseBody = new PageImpl<>(Arrays.asList(userDTO, userDTO2));
        when(userService.findAll(pageable)).thenReturn(expectedResponseBody);
        //then
        MvcResult mvcResult = mockMvc.perform(get("/users")
                        .contentType("application/json")
                        .param("page", "1")
                        .param("size", "2")
                        .param("sort", "name,desc"))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    void delete_validUserWithContract_shouldReturnTrue() throws Exception {
        //given
        int id = 1;
        // when
        when(userService.delete(id)).thenReturn(true);
        //then
        mockMvc.perform(delete("/users/{id}", id)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    void delete_invalidData_shouldReturnFalse() throws Exception {
        //given && when
        int invalidId = 5;
        when(userService.delete(invalidId)).thenReturn(false);
        //then
        mockMvc.perform(delete("/users/{id}", invalidId)
                        .contentType("application/json"))
                .andExpect(status().isNotModified());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    void update_validData_shouldReturnUpdatedUserDTO() throws Exception {
        //given
        int userId = 1;
        String name = "updatedName";
        RoleDTO roleDTO = RoleDTO.builder().id(3).name("contractor").build();
        UserDTO expectedResponseBody = UserDTO.builder().id(userId).name(name).password("pass").role(roleDTO).email("email").build();
        //when
        when(userService.update(expectedResponseBody)).thenReturn(expectedResponseBody);
        // then
        MvcResult mvcResult = mockMvc.perform(put("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponseBody)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    void add_validData_shouldReturnNewUserDTO() throws Exception {
        //given
        UserDTO expectedResponseBody = UserDTO.builder().id(5).name("newUser").password("pass").role(RoleDTO.builder().id(2).name("customer").build()).email("email").build();
        //when
        when(userService.add(expectedResponseBody)).thenReturn(Optional.of(expectedResponseBody));
        //then
        MvcResult mvcResult = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedResponseBody)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    void assignCertificate_validDate_shouldReturnCertificateList() throws Exception {
        //given
        int userId = 3;
        int certificateId = 2;
        CertificateDTO certificateDTO = CertificateDTO.builder().build();
        List<CertificateDTO> expectedResponseBody = Arrays.asList(certificateDTO, certificateDTO, certificateDTO);
        //when
        when(userService.assignCertificate(userId, certificateId))
                .thenReturn(expectedResponseBody);
        //then
        MvcResult mvcResult = mockMvc.perform(post("/users/assignCertificate")
                        .contentType("application/json")
                        .param("userId", String.valueOf(userId))
                        .param("certificateId", String.valueOf(certificateId)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    void removeCertificate_validData_shouldDeleteUserCertificate() throws Exception {
        //given
        int userId = 3;
        int certificateId = 1;
        CertificateDTO certificateDTO = CertificateDTO.builder().build();
        List<CertificateDTO> expectedResponseBody = Arrays.asList(certificateDTO);
        //when
        when(userService.removeCertificate(userId, certificateId))
                .thenReturn(expectedResponseBody);
        //then
        MvcResult mvcResult = mockMvc.perform(post("/users/removeCertificate")
                        .contentType("application/json")
                        .param("userId", String.valueOf(userId))
                        .param("certificateId", String.valueOf(certificateId)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }
}