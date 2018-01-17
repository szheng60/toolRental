package com.toolrental.service;

import com.toolrental.dao.ICustomerReport;
import com.toolrental.model.CustomerReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xinyu on 11/4/2017.
 */
@Service
public class CustomerReportService implements ICustomerReportService {
    @Autowired
    private ICustomerReport customerReportDao;

    @Override
    public List<CustomerReport> retrieveAllCustomerReport(int month) {
        return customerReportDao.retrieveAllCustomerReport(month);
    }
}
