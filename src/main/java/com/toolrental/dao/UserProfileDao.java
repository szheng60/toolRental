package com.toolrental.dao;

import com.toolrental.model.PhoneNumber;
import com.toolrental.model.UserDetail;
import com.toolrental.model.UserReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by xinyu on 11/1/2017.
 */
@Repository
public class UserProfileDao implements IProfile {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String retrievePhoneNumber = "SELECT sub_type, CONCAT('(',area_code,')',phone_number,IFNULL(CONCAT(' -x',extension),'')) AS phone FROM phonenumber where customerEmail=?;";
    private String retrieveUserDetail = "SELECT c.userEmail,CONCAT(u.first_name,' ',IFNULL(u.mid_name, ''),' ',u.last_name) AS fullName,CONCAT(c.street,', ',c.city,', ',c.state,' ',c.zip_code) AS address FROM Customer c INNER JOIN User u ON c.userEmail = u.email WHERE c.userEmail = ?;";
    private String retrieveUserReservation = "SELECT r.reservationID,GROUP_CONCAT(IF(t.power_source = 'Manual','',CONCAT(t.power_source, ' ')),t.sub_option,' ',t.sub_type) AS tools,r.start_date AS start_date,r.end_date AS end_date,IFNULL(cl1.first_name, '') AS pick_up_clerk,IFNULL(cl2.first_name, '') AS drop_off_clerk,DATEDIFF(r.end_date, r.start_date) AS number_of_days,ROUND(SUM(.4 * t.price),2) AS deposit_price,ROUND(SUM(DATEDIFF(r.end_date, r.start_date) *(t.price * .15)),2) AS rental_price FROM Reservation r INNER JOIN ReservationHistory rh ON rh.reservationID = r.reservationID INNER JOIN Tool t ON rh.toolID = t.toolID LEFT JOIN User cl1 ON cl1.email = r.pickup_clerk LEFT JOIN User cl2 ON cl2.email = r.dropoff_clerk WHERE r.customer_email = ? GROUP BY r.reservationID,cl1.first_name,cl2.first_name,r.start_date,r.end_date;";

    @Override
    public List<PhoneNumber> retrievePhoneNumbers(String email) {
        return jdbcTemplate.query(retrievePhoneNumber, new RowMapper<PhoneNumber>() {

            @Override
            public PhoneNumber mapRow(ResultSet rs, int i) throws SQLException {
                PhoneNumber pn = new PhoneNumber();
                pn.setSubType(rs.getString("sub_type"));
                pn.setPhoneNumber(rs.getString("phone"));
                return pn;
            }
        }, email);
    }

    @Override
    public UserDetail retrieveUserDetail(String email) {
        return jdbcTemplate.queryForObject(retrieveUserDetail, new RowMapper<UserDetail>() {
            @Override
            public UserDetail mapRow(ResultSet rs, int i) throws SQLException {
                UserDetail ud = new UserDetail();
                ud.setUserEmail(rs.getString("userEmail"));
                ud.setFullName(rs.getString("fullName"));
                ud.setFullAddress(rs.getString("address"));
                return ud;
            }
        }, email);
    }

    @Override
    public List<UserReservation> retrieveUserReservation(String email) {
        return jdbcTemplate.query(retrieveUserReservation, new RowMapper<UserReservation>() {
            @Override
            public UserReservation mapRow(ResultSet rs, int i) throws SQLException {
                UserReservation ur = new UserReservation();
                ur.setReservationId(rs.getInt("reservationID"));
                ur.setTools(rs.getString("tools"));
                ur.setStartDate(rs.getDate("start_date"));
                ur.setEndDate(rs.getDate("end_date"));
                ur.setPickUpClerk(rs.getString("pick_up_clerk"));
                ur.setDropOffClerk(rs.getString("drop_off_clerk"));
                ur.setNumberOfDays(rs.getInt("number_of_days"));
                ur.setTotalDepositPrice(rs.getDouble("deposit_price"));
                ur.setTotalRentalPrice(rs.getDouble("rental_price"));
                return ur;
            }
        }, email);
    }
}
