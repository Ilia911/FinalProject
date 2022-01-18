package com.itrex.java.lab.service.util;

import com.itrex.java.lab.entity.CustomerReportPageParameter;
import com.itrex.java.lab.exeption.ServiceException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class CustomerReportPageParameterValidator {

    private static final int MAX_PAGE_SIZE = 1000;

    public void validatePageParameters(CustomerReportPageParameter parameters) throws ServiceException {

        validateDates(parameters.getFirstStartContractDate(), parameters.getLastStartContractDate());
        validateContractCount(parameters.getStartWithContractCount());
        validateSize(parameters.getSize());
    }

    private void validateDates(String firstDate, String lastDate) throws ServiceException {
        LocalDate start;
        LocalDate end;

        start = validateDate(firstDate);
        end = validateDate(lastDate);

        if (end.isBefore(start)) {
            throw new ServiceException(String.format("Last contract start date '%S' should be after first " +
                    "contract start date '%s'", end, start));
        }
    }

    private LocalDate validateDate(String date) throws ServiceException {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    private void validateContractCount(int count) throws ServiceException {
        if (count < 0) {
            throw new ServiceException(String.format("Contract count %s is not valid. It should not be negative", count));
        }
    }

    private void validateSize(int size) throws ServiceException {
        if (size < 0 || size > MAX_PAGE_SIZE) {
            throw new ServiceException(String.format("Page size %s is not valid. It should not be negative or more " +
                    "then %s", size, MAX_PAGE_SIZE));
        }
    }
}
