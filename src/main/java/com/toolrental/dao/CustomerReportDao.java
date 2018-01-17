package com.toolrental.dao;

import com.toolrental.model.CustomerReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by xinyu on 11/4/2017.
 */
@Repository
public class CustomerReportDao implements ICustomerReport {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CustomerReport> retrieveAllCustomerReport(int month) {
        return jdbcTemplate.query(getSQLQueryForRetrieveAllCustomerReportByMonth(month), new RowMapper<CustomerReport>() {
            @Override
            public CustomerReport mapRow(ResultSet rs, int i) throws SQLException {
                CustomerReport cr = new CustomerReport();
                cr.setCustomerId(rs.getInt("customer_id"));
                cr.setProfileEmail(rs.getString("email"));
                cr.setFirstName(rs.getString("first_name"));
                cr.setMidName(rs.getString("mid_name"));
                cr.setLastName(rs.getString("last_name"));
                cr.setEmail(rs.getString("email"));
                cr.setPhone(rs.getString("phone"));
                cr.setTotalReservations(rs.getInt("total_num_of_reservations"));
                cr.setTotalToolsRented(rs.getInt("total_num_of_tools_rented"));
                return cr;
            }
        });
    }

    private String getSQLQueryForRetrieveAllCustomerReportByMonth(int month) {
        return "SELECT\n" +
                "c.customer_id,\n" +
                "u.first_name,\n" +
                "u.mid_name,\n" +
                "u.last_name,\n" +
                "u.email,\n" +
                "CONCAT('(',ph.area_code,')',ph.phone_number) AS phone,\n" +
                "a.total_num_of_reservations,\n" +
                "b.total_num_of_tools_rented\n" +
                "FROM Customer c\n" +
                "INNER JOIN USER u ON u.email = c.userEmail\n" +
                "INNER JOIN PhoneNumber ph ON c.primary_phone = ph.sub_type AND ph. customerEmail = c.userEmail\n" +
                "INNER JOIN\n" +
                "(\n" +
                "SELECT r.customer_email AS email,\n" +
                "COUNT(r.reservationID) AS total_num_of_reservations\n" +
                "FROM\n" +
                "Reservation r\n" +
                "WHERE\n" +
                "MONTH(r.start_date) = '" + month + "'\n" +
                "GROUP BY\n" +
                "r.customer_email\n" +
                ") a\n" +
                "INNER JOIN\n" +
                "(\n" +
                "SELECT r.customer_email AS email,\n" +
                "COUNT(rh.toolID) AS total_num_of_tools_rented\n" +
                "FROM\n" +
                "ReservationHistory rh\n" +
                "INNER JOIN Reservation r ON\n" +
                "r.reservationID = rh.reservationID\n" +
                "WHERE\n" +
                "MONTH(r.end_date) = '" + month + "'\n" +
                "GROUP BY\n" +
                "r.customer_email\n" +
                ") b\n" +
                "ON\n" +
                "a.email = b.email AND u.email = a.email";
    }
}
