package com.toolrental.dao;

import com.toolrental.model.CreditCard;
import com.toolrental.model.RentalContract;
import com.toolrental.model.Reservation;
import com.toolrental.model.ReservationDetail;

import java.util.List;

/**
 * Created by xinyu on 11/5/2017.
 */
public interface IReservation {
    List<Reservation> retrieveReservationList();
    List<Reservation> retrieveDropOffList();
    ReservationDetail retrieveReservationDetail(int reservationId);
    boolean updateCreditCard(CreditCard newCreditCard, String customerId);
    RentalContract retrieveRentalContract(int reservationId, String clerkEmail);
    boolean rentalContractPickUpConfirmed(RentalContract rentalContract, String clerkEmail);
    boolean rentalContractDropOffConfirmed(RentalContract rentalContract, String clerkEmail);
}
