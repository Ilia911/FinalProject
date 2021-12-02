package com.itrex.java.lab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrex.java.lab.entity.dto.RoleDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "Admin", roles = {"ADMIN"})
class RoleControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void find_validData_shouldReturnRole() throws Exception {
        //given
        int id = 2;
        String name = "ROLE_CUSTOMER";
        RoleDTO expectedResponseBody = RoleDTO.builder().id(id).name(name).build();
        // when
        when(roleService.find(id)).thenReturn(Optional.of(expectedResponseBody));
        // then
        MvcResult mvcResult = mockMvc.perform(get("/roles/{id}", id).contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }

    @Test
    void find_invalidData_shouldReturnEmptyOptional() throws Exception {
        //given
        int id = -2;
        // when
        when(roleService.find(id)).thenReturn(Optional.empty());
        // then
        mockMvc.perform(get("/roles/{id}", id).contentType("application/json"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findAllRoles_validData_shouldReturnRoleList() throws Exception {
        //given
        RoleDTO roleDTO = RoleDTO.builder().build();
        List<RoleDTO> expectedResponseBody = Arrays.asList(roleDTO, roleDTO);
        // when
        when(roleService.findAll()).thenReturn(expectedResponseBody);
        // then
        MvcResult mvcResult = mockMvc.perform(get("/roles").contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedResponseBody), actualResponseBody);
    }
}