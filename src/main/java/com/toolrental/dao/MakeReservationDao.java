package com.toolrental.dao;

import com.toolrental.form.MakeReservationForm;
import com.toolrental.form.ToolAvailabilityForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xinyu on 11/2/2017.
 */
@Repository
public class MakeReservationDao implements IMakeReservation {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Integer> getListOfUpcomingReturnToolIds(ToolAvailabilityForm toolAvailabilityForm) {
        return jdbcTemplate.query(getQueryForListOfUpcomingReturnToolIds(toolAvailabilityForm), new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getInt("toolID");
            }
        });
    }

    @Transactional
    @Override
    public int confirmReservation(MakeReservationForm makeReservationForm) {
        String addReservationQuery = getQueryForInsertIntoReservation(makeReservationForm.getCustomerEmail(), makeReservationForm.getStartDate(), makeReservationForm.getEndDate());
        KeyHolder holder = new GeneratedKeyHolder();
        PreparedStatementCreator creator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement addReservation = con.prepareStatement(addReservationQuery, new String[] {"reservationID"});
                return addReservation;
            }
        };
        jdbcTemplate.update(creator, holder);
        int reservationId = holder.getKey().intValue();
        if (reservationId > 0) {
            List<Integer> toolIds = makeReservationForm.getToolIds();
            if (toolIds.size() > 0) {
                for (int toolId : toolIds) {
                    jdbcTemplate.update(getQueryForInsertIntoReservationHistory(reservationId, toolId));
                }
            }
        }
        return reservationId;
    }

    private String getQueryForListOfUpcomingReturnToolIds(ToolAvailabilityForm toolAvailabilityForm) {
        return "SELECT\n" +
                "t.toolID\n" +
                "FROM\n" +
                "Reservation r\n" +
                "INNER JOIN ReservationHistory rh ON\n" +
                "r.reservationID = rh.reservationID\n" +
                "INNER JOIN Tool t ON t.toolID = rh.toolID\n" +
                "WHERE " + getCheckAvailableToolSQL2(toolAvailabilityForm);
    }

    private String getCheckAvailableToolSQL2(ToolAvailabilityForm toolAvailabilityForm) {
        StringBuilder checkAvailableToolSQL2 = new StringBuilder();
        String toolType = toolAvailabilityForm.getToolType();
        String powerSource = toolAvailabilityForm.getPowerSource();
        String subType = toolAvailabilityForm.getSubType();
        String startDate = toolAvailabilityForm.getStartDate();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String endDate = dt.format(toolAvailabilityForm.getEndDate());
        String customSearch = toolAvailabilityForm.getCustomSearch();

        checkAvailableToolSQL2.append("t.type='" + toolType + "'");
        if (powerSource != null) {
            checkAvailableToolSQL2.append(" AND t.power_source='" + powerSource + "'");
        }
        if (subType != null) {
            checkAvailableToolSQL2.append(" AND t.sub_type='" + subType + "'");
        }
        if (customSearch != null) {
            checkAvailableToolSQL2.append(" AND(t.type LIKE '" + customSearch + "' OR t.sub_type LIKE '" + customSearch + "' OR t.sub_option LIKE '" + customSearch + "')");
        }
        checkAvailableToolSQL2.append(getCheckAvailableToolSQL3(endDate));
        return checkAvailableToolSQL2.toString();
    }

    private String getCheckAvailableToolSQL3(String endDate) {
        return " AND '" + endDate + "'< DATE_ADD(NOW(), INTERVAL 1 DAY) AND '" + endDate + "'>= CURDATE();";
    }

    private String getQueryForInsertIntoReservation(String customerEmail, String startDate, String endDate) {
        return "INSERT INTO reservation(customer_email, start_date, end_date) VALUES('" + customerEmail + "', '" + startDate + "', '" + endDate + "');";
    }

    private String getQueryForInsertIntoReservationHistory(int reservationId, int toolId) {
        return "INSERT INTO reservationhistory VALUES(" + reservationId + "," + toolId + ");";
    }
}
