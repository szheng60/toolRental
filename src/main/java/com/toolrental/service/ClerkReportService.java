package com.toolrental.service;

import com.toolrental.dao.IClerkReport;
import com.toolrental.model.ClerkReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xinyu on 11/4/2017.
 */
@Service
public class ClerkReportService implements IClerkReportService {
    @Autowired
    private IClerkReport clerkReportDao;

    @Override
    public List<ClerkReport> retrieveAllClerkReport(int month) {
        return clerkReportDao.retrieveAllClerkReport(month);
    }
}
