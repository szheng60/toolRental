package com.toolrental.service;

import com.toolrental.dao.IReservation;
import com.toolrental.model.CreditCard;
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
public class PickUpReservationService implements IPickUpReservationService {
    @Autowired
    private IReservation reservationDao;

    @Override
    public List<Reservation> retrieveReservationList() {
        return reservationDao.retrieveReservationList();
    }

    @Override
    public ReservationDetail retrieveReservationDetail(int reservationId) {
        return reservationDao.retrieveReservationDetail(reservationId);
    }

    @Override
    public boolean updateCreditCard(CreditCard newCreditCard, String customerId) {
        return reservationDao.updateCreditCard(newCreditCard, customerId);
    }

    @Override
    public RentalContract retrieveRentalContract(int reservationId, String clerkEmail) {
        return reservationDao.retrieveRentalContract(reservationId, clerkEmail);
    }

    @Override
    public boolean rentalContractConfirmed(RentalContract rentalContract, String clerkEmail) {
        return reservationDao.rentalContractPickUpConfirmed(rentalContract, clerkEmail);
    }
}
