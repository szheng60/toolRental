package com.toolrental.service;

import com.toolrental.form.ToolAvailabilityForm;
import com.toolrental.model.ToolDetail;

import java.util.List;

/**
 * Created by xinyu on 11/1/2017.
 */
public interface IAvailableToolService {
    List<ToolDetail> availableToolList(ToolAvailabilityForm toolAvailabilityForm);
}
