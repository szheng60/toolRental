package com.toolrental.dao;

import com.toolrental.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by xinyu on 11/5/2017.
 */
@Repository
public class ReservationDao implements IReservation {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Reservation> retrieveReservationList() {
        return jdbcTemplate.query(getQueryForListOfReservation(), new RowMapper<Reservation>() {
            @Override
            public Reservation mapRow(ResultSet rs, int i) throws SQLException {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getInt("reservationID"));
                reservation.setCustomerShortName(rs.getString("customer"));
                reservation.setCustomerId(rs.getInt("customer_id"));
                reservation.setStartDate(rs.getDate("start_date"));
                reservation.setEndDate(rs.getDate("end_date"));
                return reservation;
            }
        });
    }

    @Override
    public List<Reservation> retrieveDropOffList() {
        return jdbcTemplate.query(getQueryForListOfDropoff(), new RowMapper<Reservation>() {
            @Override
            public Reservation mapRow(ResultSet rs, int i) throws SQLException {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getInt("reservationID"));
                reservation.setCustomerShortName(rs.getString("customer"));
                reservation.setCustomerId(rs.getInt("customer_id"));
                reservation.setStartDate(rs.getDate("start_date"));
                reservation.setEndDate(rs.getDate("end_date"));
                return reservation;
            }
        });
    }

    @Override
    public ReservationDetail retrieveReservationDetail(int reservationId) {
        return jdbcTemplate.queryForObject(getQueryForReservationDetail(reservationId), new RowMapper<ReservationDetail>() {
            @Override
            public ReservationDetail mapRow(ResultSet rs, int i) throws SQLException {
                ReservationDetail rd = new ReservationDetail();
                rd.setReservationId(rs.getInt("reservationID"));
                rd.setCustomerFullName(rs.getString("fullName"));
                rd.setTotalDeposit(rs.getDouble("deposit_price"));
                rd.setTotalRentalPrice(rs.getDouble("rental_price"));
                return rd;
            }
        });
    }

    @Override
    public boolean updateCreditCard(CreditCard newCreditCard, String customerId) {
        int result = jdbcTemplate.update(getQueryForUpdateCreditCard(newCreditCard, customerId));
        return result == 1;
    }

    @Override
    public RentalContract retrieveRentalContract(int reservationId, String clerkEmail) {
        String pickUpClerkName = jdbcTemplate.queryForObject(getQueryForRentalContractClerkInfo(clerkEmail), String.class);
        RentalContract rc = jdbcTemplate.queryForObject(getQueryForRentalContractInfo(reservationId), new RowMapper<RentalContract>() {
            @Override
            public RentalContract mapRow(ResultSet rs, int i) throws SQLException {
                RentalContract rc = new RentalContract();
                rc.setCustomerName(rs.getString("customerName"));
                rc.setCreditCardNumber(rs.getString("creditCardNumber"));
                rc.setStartDate(rs.getDate("start_date"));
                rc.setEndDate(rs.getDate("end_date"));
                return rc;
            }
        });
        rc.setPickUpClerkName(pickUpClerkName);

        List<ToolDetail> toolDetailList = jdbcTemplate.query(getQueryForRentalContractToolList(reservationId), new RowMapper<ToolDetail>() {
            @Override
            public ToolDetail mapRow(ResultSet rs, int i) throws SQLException {
                ToolDetail td = new ToolDetail();
                td.setToolId(rs.getInt("toolID"));
                td.setShortDescription(rs.getString("description"));
                td.setRentalPrice(rs.getDouble("rental_price"));
                td.setDepositPrice(rs.getDouble("deposit_price"));
                return td;
            }
        });

        rc.setReservedTools(toolDetailList);
        rc.setReservationId(reservationId);
        return rc;
    }

    @Override
    public boolean rentalContractPickUpConfirmed(RentalContract rentalContract, String clerkEmail) {
        int result = jdbcTemplate.update(getQueryForRentalContractPickUpClerk(clerkEmail, rentalContract.getReservationId()));
        if (result == 0) {
            return false;
        }
//        for(ToolDetail td: rentalContract.getReservedTools()) {
//            result = jdbcTemplate.update(getQueryForRentalContractReservationHistory(rentalContract.getReservationId(), td.getToolId()));
//            if (result == 0) {
//                return false;
//            }
//        }
        return true;
    }

    @Override
    public boolean rentalContractDropOffConfirmed(RentalContract rentalContract, String clerkEmail) {
        int result = jdbcTemplate.update(getQueryForRentalContractDropOffClerk(clerkEmail, rentalContract.getReservationId()));
        return result == 1;
    }

    private String getQueryForListOfReservation() {
        return "SELECT\n" +
                "r.reservationID,\n" +
                "CONCAT(LEFT(u.first_name, 1),u.last_name) AS customer,\n" +
                "c.customer_id,\n" +
                "r.start_date,\n" +
                "r.end_date\n" +
                "FROM\n" +
                "User u\n" +
                "INNER JOIN Customer c ON\n" +
                "c.userEmail = u.email\n" +
                "INNER JOIN Reservation r ON\n" +
                "r.customer_email = u.email\n" +
                "WHERE\n" +
                "r.pickup_clerk IS NULL OR r.pickup_clerk = '';";
    }

    private String getQueryForListOfDropoff() {
        return "SELECT\n" +
                "r.reservationID,\n" +
                "CONCAT(LEFT(u.first_name, 1),u.last_name) AS Customer,\n" +
                "c.customer_id,\n" +
                "r.start_date,\n" +
                "r.end_date\n" +
                "FROM\n" +
                "User u\n" +
                "INNER JOIN Customer c ON c.userEmail = u.email\n" +
                "INNER JOIN Reservation r ON r.customer_email = u.email\n" +
                "WHERE\n" +
                "r.pickup_clerk IS NOT NULL AND(\n" +
                "r.dropoff_clerk IS NULL OR r.dropoff_clerk = '');";
    }

    private String getQueryForReservationDetail(int reservationId) {
        return "SELECT\n" +
                "r.reservationID,\n" +
                "CONCAT(u.first_name, ' ', u.last_name) AS fullName,\n" +
                "ROUND(SUM(.4 * t.price),2) AS deposit_price,\n" +
                "ROUND(SUM(DATEDIFF(r.end_date, r.start_date) *(t.price * .15)),2) AS rental_price\n" +
                "FROM\n" +
                "Reservation r\n" +
                "INNER JOIN ReservationHistory rh ON\n" +
                "rh.reservationID = r.reservationID\n" +
                "INNER JOIN Tool t ON rh.toolID = t.toolID\n" +
                "INNER JOIN User u ON u.email = r.customer_email\n" +
                "WHERE r.reservationID = '" + reservationId + "'\n" +
                "GROUP BY\n" +
                "r.reservationID,\n" +
                "r.start_date,\n" +
                "r.end_date,\n" +
                "u.first_name,\n" +
                "    u.last_name;";
    }

    private String getQueryForUpdateCreditCard(CreditCard theCreditCard, String id) {
        return "UPDATE CreditCard cr\n" +
                "JOIN Customer cu ON (cu.card_no=cr.number) SET " +
                "cr.number='" + theCreditCard.getNumber() +
                "', cr.name_on_card='" + theCreditCard.getNameOnCard() +
                "', cr.exp_month='" + theCreditCard.getExpMonth() +
                "', cr.exp_year='" + theCreditCard.getExpYear() +
                "', cr.cvc='" + theCreditCard.getCvc() +
                "' WHERE cu.customer_id=" + id + ";";
    }

    private String getQueryForRentalContractInfo(int reservationId) {
        return "SELECT\n" +
                "CONCAT(u.first_name, ' ', u.last_name) AS customerName,\n" +
                "cr.number AS creditCardNumber,\n" +
                "r.start_date,\n" +
                "r.end_date\n" +
                "FROM\n" +
                "Reservation r\n" +
                "INNER JOIN Customer cu ON r.customer_email = cu.userEmail\n" +
                "INNER JOIN CreditCard cr ON cu.card_no = cr.number\n" +
                "INNER JOIN User u ON u.email = r.customer_email\n" +
                "WHERE r.reservationID =" + reservationId + ";";
    }
    private String getQueryForRentalContractClerkInfo(String clerkEmail) {
        return "SELECT\n" +
                "CONCAT(u.first_name, ' ', u.last_name) AS pickUpClerk\n" +
                "FROM\n" +
                "User u\n" +
                "WHERE\n" +
                "u.email ='" + clerkEmail + "';";
    }
    private String getQueryForRentalContractToolList(int reservationId) {
        return "SELECT\n" +
                "t.toolID,\n" +
                "CONCAT(IF(t.power_source = 'Manual','',CONCAT(t.power_source, ' ')\n" +
                "),t.sub_option,' ',t.sub_type) AS description,\n" +
                "ROUND((t.price * .15),2) AS rental_price,\n" +
                "ROUND((t.price * .40),2) AS deposit_price\n" +
                "FROM\n" +
                "Tool t\n" +
                "INNER JOIN ReservationHistory rh ON rh.toolID = t.toolID\n" +
                "WHERE\n" +
                "rh.reservationID =" + reservationId + ";";
    }

    private String getQueryForRentalContractPickUpClerk(String clerkEmail, int reservationId) {
        return "UPDATE\n" +
                "Reservation r\n" +
                "SET\n" +
                "r.pickup_clerk = '" + clerkEmail + "'\n" +
                "WHERE\n" +
                "r.reservationID = '" + reservationId + "';";
    }

    private String getQueryForRentalContractDropOffClerk(String clerkEmail, int reservationId) {
        return "UPDATE\n" +
                "Reservation r\n" +
                "SET\n" +
                "r.dropoff_clerk = '" + clerkEmail + "'\n" +
                "WHERE\n" +
                "r.reservationID = '" + reservationId + "';";
    }

    private String getQueryForRentalContractReservationHistory(int reservationId, int toolId) {
        return "INSERT INTO ReservationHistory(reservationID, toolID)\n" +
                "VALUES('" + reservationId + "', '" + toolId + "');";
    }
}
