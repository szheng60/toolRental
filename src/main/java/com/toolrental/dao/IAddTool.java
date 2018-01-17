package com.toolrental.dao;

import com.toolrental.form.AddToolForm;

/**
 * Created by xinyu on 11/17/2017.
 */
public interface IAddTool {
    int addTool(AddToolForm addToolForm);
    boolean addSpecificTool(AddToolForm addToolForm, int toolId);
    int removeTool(int toolId);
}
