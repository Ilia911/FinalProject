package com.itrex.java.lab.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

class AuthControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    void login_validData_shouldLogIn() throws Exception {
        //given
        String loginPath = "/login";
        String username = "admin@gmail.com";
        String password = "password";
        //when && then
        mockMvc.perform(formLogin(loginPath).user(username).password(password)).andExpect(authenticated());
    }

    @Test
    @WithAnonymousUser
    void login_invalidData_shouldNotLogIn() throws Exception {
        //given
        String loginPath = "/login";
        String username = "admin@gmail.com";
        String password = "invalid";
        //when && then
        mockMvc.perform(formLogin(loginPath).user(username).password(password)).andExpect(unauthenticated());
    }

    @Test
    @PreAuthorize("authenticated")
    void logout_validData_shouldLogOut() throws Exception {
        //given
        String logoutPath = "/logout";
        //when & then
        mockMvc.perform(logout(logoutPath)).andExpect(unauthenticated());
    }
}