package com.toolrental.service;

import com.toolrental.model.CreditCard;
import com.toolrental.model.RentalContract;
import com.toolrental.model.Reservation;
import com.toolrental.model.ReservationDetail;

import java.util.List;

/**
 * Created by xinyu on 11/5/2017.
 */
public interface IDropOffReservationService {
    List<Reservation> retrieveReservationList();
    ReservationDetail retrieveReservationDetail(int reservationId);
    RentalContract retrieveRentalContract(int reservationId, String clerkEmail);
    boolean rentalContractConfirmed(RentalContract rentalContract, String clerkEmail);
}
