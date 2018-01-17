package com.toolrental.service;

import com.toolrental.dao.IAddTool;
import com.toolrental.form.AddToolForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created by xinyu on 11/17/2017.
 */
@Service
public class AddToolService implements IAddToolService {
    @Autowired
    private IAddTool addToolDao;

    @Transactional
    @Override
    public boolean addTool(AddToolForm addToolForm) {
        int toolId = addToolDao.addTool(addToolForm);
        if (addToolForm.getToolSubType().equalsIgnoreCase("wrench")) {
            return true;
        }
        return addToolDao.addSpecificTool(addToolForm, toolId);
    }
}
