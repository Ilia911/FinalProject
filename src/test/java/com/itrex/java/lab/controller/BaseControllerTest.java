package com.itrex.java.lab.controller;

import com.itrex.java.lab.service.CertificateService;
import com.itrex.java.lab.service.ContractService;
import com.itrex.java.lab.service.OfferService;
import com.itrex.java.lab.service.RoleService;
import com.itrex.java.lab.service.UserService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest
public abstract class BaseControllerTest {
    @MockBean
    protected CertificateService certificateService;
    @MockBean
    protected ContractService contractService;
    @MockBean
    protected OfferService offerService;
    @MockBean
    protected RoleService roleService;
    @MockBean
    protected UserService userService;
}
