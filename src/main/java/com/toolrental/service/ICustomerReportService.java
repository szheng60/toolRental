package com.toolrental.service;

import com.toolrental.model.CustomerReport;

import java.util.List;

/**
 * Created by xinyu on 11/4/2017.
 */
public interface ICustomerReportService {
    List<CustomerReport> retrieveAllCustomerReport(int month);
}
