package com.itrex.java.lab.controller;

import com.itrex.java.lab.entity.CustomerReportPageParameter;
import com.itrex.java.lab.entity.OfferReportPageParameter;
import com.itrex.java.lab.entity.dto.CustomerReportDTO;
import com.itrex.java.lab.entity.dto.OfferReportDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.service.ReporterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReporterController {

    private final ReporterService service;

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('report:read')")
    public List<CustomerReportDTO> find(CustomerReportPageParameter parameters) throws ServiceException {

        return service.findAllCustomer(parameters);
    }

    @GetMapping("/offers")
    @PreAuthorize("hasAuthority('report:read')")
    public List<OfferReportDTO> get(OfferReportPageParameter parameter) throws ServiceException {

        return service.getOfferReport(parameter);
    }
}
