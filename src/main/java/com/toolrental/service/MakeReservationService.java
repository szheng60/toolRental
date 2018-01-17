package com.toolrental.service;

import com.toolrental.dao.IMakeReservation;
import com.toolrental.form.MakeReservationForm;
import com.toolrental.form.ToolAvailabilityForm;
import com.toolrental.model.ToolDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xinyu on 11/5/2017.
 */
@Service
public class MakeReservationService implements IMakeReservationService {

    @Autowired
    private IMakeReservation makeReservationDao;

    @Override
    public List<Integer> getListOfUpcomingReturnToolIds(ToolAvailabilityForm toolAvailabilityForm) {
        return makeReservationDao.getListOfUpcomingReturnToolIds(toolAvailabilityForm);
    }

    @Override
    public int confirmReservation(MakeReservationForm makeReservationForm) {
        return makeReservationDao.confirmReservation(makeReservationForm);
    }
}
