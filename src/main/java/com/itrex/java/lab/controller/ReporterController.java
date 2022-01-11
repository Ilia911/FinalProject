package com.itrex.java.lab.controller;

import com.itrex.java.lab.entity.dto.CustomerReportDTO;
import com.itrex.java.lab.entity.dto.OfferReportDTO;
import com.itrex.java.lab.service.ReporterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReporterController {

    @Autowired
    private ReporterService service;

    @GetMapping("/customers")
    @PreAuthorize("hasAuthority('report:read')")
    public List<CustomerReportDTO> find(@RequestParam(name = "firstStartContractDate") String firstStartContractDate,
                                        @RequestParam(name = "lastStartContractDate") String lastStartContractDate,
                                        @RequestParam(name = "startWithContractCount", defaultValue = "2147483647")
                                                    int startWithContractCount,
                                        @RequestParam(name = "size", defaultValue = "20") int size) {

        return service.findAllCustomer(LocalDate.parse(firstStartContractDate),
                LocalDate.parse(lastStartContractDate), startWithContractCount, size);
    }

    @GetMapping("/offers")
    @PreAuthorize("hasAuthority('report:read')")
    public List<OfferReportDTO> get(@RequestParam(name = "firstStartContractDate") String firstStartContractDate,
                                    @RequestParam(name = "lastStartContractDate") String lastStartContractDate,
                                    @RequestParam(name = "startWithContractId", defaultValue = "0") int startWithId,
                                    @RequestParam(name = "size", defaultValue = "20") int size) {

        return service.getOfferReport(LocalDate.parse(firstStartContractDate),
                LocalDate.parse(lastStartContractDate), startWithId, size);
    }
}
