package com.toolrental.dao;

import com.toolrental.form.ToolAvailabilityForm;
import com.toolrental.model.ToolDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by xinyu on 11/1/2017.
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class ToolAvailabilityDao implements IToolAvailability{

    private String checkAvailableToolSQL1 = "SELECT t.toolID,CONCAT(IF(t.power_source = 'Manual','',CONCAT(t.power_source, ' ')),t.sub_option,' ',t.sub_type) AS description,ROUND((t.price * .15),2) AS rental_price,ROUND((t.price * .40),2) AS deposit_price FROM Tool t WHERE ";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IToolDetail toolDetailDao;

    @Override
    public List<ToolDetail> availableToolList(ToolAvailabilityForm toolAvailabilityForm) {
        final String toolType = toolAvailabilityForm.getToolType();
        final String toolSubType = toolAvailabilityForm.getSubType();
        final String toolPowerSource = toolAvailabilityForm.getPowerSource();
        String query = checkAvailableToolSQL1 + getCheckAvailableToolSQL2(toolAvailabilityForm);
        return jdbcTemplate.query(query, new RowMapper<ToolDetail>() {
            @Override
            public ToolDetail mapRow(ResultSet rs, int i) throws SQLException {
                ToolDetail td = new ToolDetail();
                td.setToolType(toolType);
                td.setToolSubType(toolSubType);
                td.setToolPowerSource(toolPowerSource);
                td.setToolId(rs.getInt("toolID"));
                td.setShortDescription(rs.getString("description"));
                td.setRentalPrice(rs.getDouble("rental_price"));
                td.setDepositPrice(rs.getDouble("deposit_price"));
                return td;
            }
        });
    }

    private String getCheckAvailableToolSQL2(ToolAvailabilityForm toolAvailabilityForm) {
        StringBuilder checkAvailableToolSQL2 = new StringBuilder();
        String toolType = toolAvailabilityForm.getToolType();
        String powerSource = toolAvailabilityForm.getPowerSource();
        String subType = toolAvailabilityForm.getSubType();
        String startDate = toolAvailabilityForm.getStartDate();
        String endDate = toolAvailabilityForm.getEndDate();
        String customSearch = toolAvailabilityForm.getCustomSearch();
        if(!toolType.equalsIgnoreCase("all")) {
            checkAvailableToolSQL2.append("t.type='" + toolType +"'");
            if (!powerSource.equalsIgnoreCase("all")) {
                checkAvailableToolSQL2.append(" AND t.power_source='" + powerSource+"'");
            }
            if (!subType.equalsIgnoreCase("all")) {
                checkAvailableToolSQL2.append(" AND t.sub_type='" + subType+"'");
            }
            if (customSearch != null) {
                checkAvailableToolSQL2.append(" AND(t.type LIKE '" + customSearch + "' OR t.sub_type LIKE '" + customSearch + "' OR t.sub_option LIKE '" + customSearch + "')");
            }
        } else {
            checkAvailableToolSQL2.append("t.type IN ('Hand Tool', 'Garden Tool', 'Ladder Tool', 'Power Tool')");
        }
        checkAvailableToolSQL2.append(getCheckAvailableToolSQL3(startDate, endDate));
        return checkAvailableToolSQL2.toString();
    }

    private String getCheckAvailableToolSQL3(String startDate, String endDate) {
        return " AND NOT EXISTS(SELECT 1 FROM ServiceOrder s INNER JOIN ServiceHistory sh ON s.serviceID = sh.serviceID WHERE t.toolID = sh.toolID AND(s.start_date BETWEEN CAST('"
                + startDate + "' AS DATE) AND CAST('" + endDate + "' AS DATE) OR s.end_date BETWEEN CAST('"
                + startDate + "' AS DATE) AND CAST('" + endDate + "' AS DATE))) " +
                "AND NOT EXISTS(SELECT 1 FROM PurchaseOrder pu WHERE t.purchase_id = pu.purchaseID AND pu.for_sale_date BETWEEN CAST('"
                + startDate + "' AS DATE) AND CAST('" + endDate + "' AS DATE)) " +
                "AND NOT EXISTS(SELECT 1 FROM reservation r INNER JOIN reservationhistory rh ON r.reservationID = rh.reservationID WHERE " +
                "t.toolID = rh.toolID AND (r.start_date BETWEEN CAST('"
                + startDate + "' AS DATE) AND CAST('" + endDate + "' AS DATE) OR r.end_date BETWEEN CAST('"
                + startDate + "' AS DATE) AND CAST('" + endDate + "' AS DATE)));";
    }


    private String getToolDetailSQL() {
        return "SELECT\n" +
                "t.toolID,\n" +
                "CONCAT(IF(t.power_source = 'Manual','',CONCAT(t.power_source, ' ')\n" +
                "),t.sub_option,' ',t.sub_type) AS description,\n" +
                "CONCAT(\n" +
                "t.width,\n" +
                "' ',\n" +
                "t.width_unit,\n" +
                "' W x ',\n" +
                "t.len,\n" +
                "' ',\n" +
                "t.len_unit,\n" +
                "' L ',\n" +
                "t.weight,\n" +
                "t.weight_unit,\n" +
                "' #',\n" +
                "h.screw_size,\n" +
                "' ',\n" +
                "IF(t.power_source = 'Manual','',CONCAT(t.power_source, ' ')),\n" +
                "t.sub_option,' ',t.sub_type,' ',\n" +
                "' by ',t.manufacturer\n" +
                ") AS detail,\n" +
                "ROUND((t.price * .15),2) AS rental_price,\n" +
                "ROUND((t.price * .40),2) AS deposit_price\n" +
                "FROM\n" +
                "Tool t\n" +
                "INNER JOIN HandscrewDriver h ON t.toolID = h.toolID\n" +
                "WHERE\n" +
                "    t.toolID = ?;";
    }
}
