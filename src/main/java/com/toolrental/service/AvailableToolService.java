package com.toolrental.service;

import com.toolrental.dao.IToolAvailability;
import com.toolrental.form.ToolAvailabilityForm;
import com.toolrental.model.ToolDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xinyu on 11/1/2017.
 */
@Service
public class AvailableToolService implements IAvailableToolService{

    @Autowired
    private IToolAvailability toolAvailabilityDao;

    @Override
    public List<ToolDetail> availableToolList(ToolAvailabilityForm toolAvailabilityForm) {
        return toolAvailabilityDao.availableToolList(toolAvailabilityForm);
    }
}
