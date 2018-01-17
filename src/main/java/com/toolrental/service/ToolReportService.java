package com.toolrental.service;

import com.toolrental.dao.IToolReport;
import com.toolrental.model.ToolReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xinyu on 11/4/2017.
 */
@Service
public class ToolReportService implements IToolReportService {
    @Autowired
    private IToolReport toolReportDao;

    @Override
    public List<ToolReport> retrieveToolReport(String filter) {
        return toolReportDao.retrieveToolReport(filter);
    }
}
