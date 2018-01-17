package com.toolrental.dao;

import com.toolrental.model.ToolReport;

import java.util.List;

/**
 * Created by xinyu on 11/4/2017.
 */
public interface IToolReport {
    List<ToolReport> retrieveToolReport(String filter);
}
