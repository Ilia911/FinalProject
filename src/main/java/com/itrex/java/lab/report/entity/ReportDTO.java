package com.itrex.java.lab.report.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReportDTO {
    List<ColumnDTO> columns;
    List<Map<String, Object>> values;
}
