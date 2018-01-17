package com.toolrental.service;

import com.toolrental.model.ClerkReport;

import java.util.List;

/**
 * Created by xinyu on 11/4/2017.
 */
public interface IClerkReportService {
    List<ClerkReport> retrieveAllClerkReport(int month);
}
