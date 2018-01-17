package com.toolrental.service;

import com.toolrental.model.ToolReport;

import java.util.List;

/**
 * Created by xinyu on 11/4/2017.
 */
public interface IToolReportService {
    List<ToolReport> retrieveToolReport(String filter);
}
