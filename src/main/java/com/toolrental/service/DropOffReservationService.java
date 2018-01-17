package com.toolrental.service;

import com.toolrental.dao.IReservation;
import com.toolrental.model.RentalContract;
import com.toolrental.model.Reservation;
import com.toolrental.model.ReservationDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xinyu on 11/5/2017.
 */
@Service
public class DropOffReservationService implements IDropOffReservationService {
    @Autowired
    private IReservation reservationDao;

    @Override
    public List<Reservation> retrieveReservationList() {

        return reservationDao.retrieveDropOffList();
    }

    @Override
    public ReservationDetail retrieveReservationDetail(int reservationId) {
        return reservationDao.retrieveReservationDetail(reservationId);
    }

    @Override
    public RentalContract retrieveRentalContract(int reservationId, String clerkEmail) {
        return reservationDao.retrieveRentalContract(reservationId, clerkEmail);
    }

    @Override
    public boolean rentalContractConfirmed(RentalContract rentalContract, String clerkEmail) {
        return reservationDao.rentalContractDropOffConfirmed(rentalContract, clerkEmail);
    }
}
