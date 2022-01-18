package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.CustomerReportPageParameter;
import com.itrex.java.lab.entity.OfferReportPageParameter;
import com.itrex.java.lab.entity.dto.CustomerReportDTO;
import com.itrex.java.lab.entity.dto.OfferReportDTO;
import com.itrex.java.lab.exeption.ServiceException;

import java.util.List;

public interface ReporterService {

    List<CustomerReportDTO> findAllCustomer(CustomerReportPageParameter parameters) throws ServiceException;

    List<OfferReportDTO> getOfferReport(OfferReportPageParameter parameter) throws ServiceException;
}
