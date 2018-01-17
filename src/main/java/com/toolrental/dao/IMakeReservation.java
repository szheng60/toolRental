package com.toolrental.dao;

import com.toolrental.form.MakeReservationForm;
import com.toolrental.form.ToolAvailabilityForm;
import com.toolrental.model.ToolDetail;

import java.util.List;

/**
 * Created by xinyu on 11/2/2017.
 */
public interface IMakeReservation {
    List<Integer> getListOfUpcomingReturnToolIds(ToolAvailabilityForm toolAvailabilityForm);
    int confirmReservation(MakeReservationForm makeReservationForm);
}
