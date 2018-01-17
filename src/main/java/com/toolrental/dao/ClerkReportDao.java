package com.toolrental.dao;

import com.toolrental.model.ClerkReport;
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
public class ClerkReportDao implements IClerkReport {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ClerkReport> retrieveAllClerkReport(int month) {
        return jdbcTemplate.query(getSQLQueryForRetrieveAllClerkReportByMonth(month), new RowMapper<ClerkReport>() {
            @Override
            public ClerkReport mapRow(ResultSet rs, int i) throws SQLException {
                ClerkReport cr = new ClerkReport();
                cr.setClerkId(rs.getInt("employee_id"));
                cr.setFirstName(rs.getString("first_name"));
                cr.setMidName(rs.getString("mid_name"));
                cr.setLastName(rs.getString("last_name"));
                cr.setEmail(rs.getString("email"));
                cr.setHireDate(rs.getDate("date_of_hire"));
                cr.setNumberOfPickups(rs.getInt("number_of_pick_up"));
                cr.setNumberOfDropoffs(rs.getInt("number_of_drop_off"));
                cr.setCombinedTotal(rs.getInt("combine_total"));
                return cr;
            }
        });
    }

    private String getSQLQueryForRetrieveAllClerkReportByMonth(int month) {
        return "SELECT\n" +
                "cl.employee_id,\n" +
                "u.first_name,\n" +
                "u.mid_name,\n" +
                "u.last_name,\n" +
                "u.email,\n" +
                "cl.date_of_hire,\n" +
                "a.number_of_pick_up,\n" +
                "b.number_of_drop_off,\n" +
                "(a.number_of_pick_up + b.number_of_drop_off) AS combine_total\n" +
                "FROM Clerk cl\n" +
                "INNER JOIN USER u ON u.email = cl.userEmail\n" +
                "INNER JOIN\n" +
                "(\n" +
                "SELECT r.pickup_clerk AS email,\n" +
                "COUNT(r.pickup_clerk) AS number_of_pick_up\n" +
                "FROM\n" +
                "Reservation r\n" +
                "WHERE\n" +
                "MONTH(r.start_date) = '" + month + "'\n" +
                "GROUP BY\n" +
                "r.pickup_clerk\n" +
                ") a\n" +
                "INNER JOIN\n" +
                "(\n" +
                "SELECT r.dropoff_clerk AS email,\n" +
                "COUNT(r.dropoff_clerk) AS number_of_drop_off\n" +
                "FROM\n" +
                "Reservation r\n" +
                "WHERE\n" +
                "MONTH(r.end_date) = '" + month + "'\n" +
                "GROUP BY\n" +
                "r.dropoff_clerk\n" +
                ") b\n" +
                "ON a.email = b.email AND u.email = a.email\n" +
                "ORDER BY a.number_of_pick_up, b.number_of_drop_off DESC;";
    }
}
