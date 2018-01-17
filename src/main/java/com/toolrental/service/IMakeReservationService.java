package com.toolrental.service;

import com.toolrental.form.MakeReservationForm;
import com.toolrental.form.ToolAvailabilityForm;
import com.toolrental.model.ToolDetail;

import java.util.List;

/**
 * Created by xinyu on 11/5/2017.
 */
public interface IMakeReservationService {
    List<Integer> getListOfUpcomingReturnToolIds(ToolAvailabilityForm toolAvailabilityForm);
    int confirmReservation(MakeReservationForm makeReservationForm);
}
