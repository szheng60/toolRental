package com.toolrental.dao;

import com.toolrental.model.ClerkReport;

import java.util.List;

/**
 * Created by xinyu on 11/4/2017.
 */
public interface IClerkReport {
    List<ClerkReport> retrieveAllClerkReport(int month);
}
