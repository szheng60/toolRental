package com.toolrental.dao;

import com.toolrental.model.ToolReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by xinyu on 11/4/2017.
 */
@Repository
public class ToolReportDao implements IToolReport {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ToolReport> retrieveToolReport(String filter) {
        return jdbcTemplate.query(getQueryForToolReport(filter), new RowMapper<ToolReport>() {
            @Override
            public ToolReport mapRow(ResultSet rs, int i) throws SQLException {
                ToolReport tr = new ToolReport();
                tr.setToolId(rs.getInt("toolID"));
                tr.setCurrentStatus(rs.getString("current_status"));

                Date date = null;
                if (!StringUtils.isEmpty(rs.getString("status_date"))) {
                    date = rs.getDate("status_date");
                }
                tr.setDate(date);
                tr.setDescription(rs.getString("description"));
                tr.setRentalProfit(rs.getDouble("rental_profit"));
                tr.setTotalCost(rs.getDouble("total_cost"));
                tr.setTotalProfit(rs.getDouble("total_profit"));
                return tr;
            }
        });
    }

    private String getQueryForToolReport(String filter) {
        String toolTypeFilter = "";
        if (!StringUtils.isEmpty(filter) && !filter.equalsIgnoreCase("All_Tool")) {

            toolTypeFilter = " AND t.type='" + filter.replace('_', ' ') + "'";
        }
        return "SELECT\n" +
                "t.toolID,\n" +
                "(\n" +
                "CASE\n" +
                "WHEN(t.purchase_id IS NOT NULL AND ph.sold_date IS NOT NULL) THEN 'sold'\n" +
                "WHEN(t.purchase_id IS NOT NULL OR t.purchase_id <> '') THEN 'for-sale'\n" +
                "WHEN(res.reservedEndDate > NOW()) THEN 'rented'\n" +
                "WHEN(res.servicedEndDate > NOW()) THEN 'in-repair' ELSE 'Available'\n" +
                "END\n" +
                ") AS current_status,\n" +
                "(\n" +
                "CASE\n" +
                "WHEN(t.purchase_id IS NOT NULL AND ph.sold_date IS NOT NULL) THEN ph.sold_date\n" +
                "WHEN(t.purchase_id IS NOT NULL OR t.purchase_id <> '') THEN ph.for_sale_date\n" +
                "WHEN(res.reservedEndDate > NOW()) THEN res.reservedEndDate\n" +
                "WHEN(res.servicedEndDate > NOW()) THEN res.servicedEndDate ELSE ''\n" +
                "END\n" +
                ") AS status_date,\n" +
                "CONCAT(IF(t.power_source = 'Manual', '', CONCAT(t.power_source, ' ')), t.sub_option, ' ', t.sub_type) AS description,\n" +
                "rental.rental_price AS rental_profit,\n" +
                "cost.total_cost,\n" +
                "(rental.rental_price - cost.total_cost) AS total_profit\n" +
                "FROM Tool t\n" +
                "LEFT JOIN PurchaseOrder ph ON ph.purchaseID = t.purchase_id\n" +
                "LEFT JOIN\n" +
                "(\n" +
                "SELECT\n" +
                "t.toolID,\n" +
                "MAX(r.end_date) AS reservedEndDate,\n" +
                "MAX(s.end_date) AS servicedEndDate\n" +
                "FROM Tool t\n" +
                "LEFT JOIN ReservationHistory rh ON rh.toolID = t.toolID\n" +
                "LEFT JOIN Reservation r ON r.reservationID = rh.reservationID\n" +
                "LEFT JOIN ServiceHistory sh ON sh.toolID = t.toolID\n" +
                "LEFT JOIN ServiceOrder s ON s.serviceID = sh.serviceID\n" +
                "GROUP BY t.toolID\n" +
                ") res\n" +
                "ON res.toolID = t.toolID\n" +
                "INNER JOIN\n" +
                "(\n" +
                "(\n" +
                "SELECT t.toolID, IFNULL(ROUND(SUM(DATEDIFF(r.end_date, r.start_date) *(t.price)), 2), 0) AS rental_price\n" +
                "FROM Tool t\n" +
                "LEFT JOIN ReservationHistory rh ON t.toolID = rh.toolID\n" +
                "LEFT JOIN Reservation r ON rh.reservationID = r.reservationID\n" +
                "GROUP BY t.toolID\n" +
                ") rental\n" +
                "LEFT JOIN\n" +
                "(\n" +
                "SELECT t.toolID, (t.price + IFNULL(a.repair_cost, 0))\n" +
                " AS total_cost\n" +
                "FROM Tool t\n" +
                "LEFT JOIN\n" +
                "(\n" +
                "SELECT sh.toolID, SUM(sh.repair_cost) AS repair_cost\n" +
                "FROM ServiceHistory sh\n" +
                "GROUP BY sh.toolID\n" +
                ") a\n" +
                "ON t.toolID = a.toolID\n" +
                ") cost\n" +
                "ON rental.toolID = cost.toolID\n" +
                ")\n" +
                "ON t.toolID = rental.toolID" + toolTypeFilter + ";";
    }
}
