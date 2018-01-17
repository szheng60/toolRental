package com.toolrental.dao;

import com.toolrental.enums.ToolDetailEnum;
import com.toolrental.model.ToolDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xinyu on 11/2/2017.
 */
@Repository
public class ToolDetailDao implements IToolDetail {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ToolDetail retrieveToolDetail(int toolId) {
        ToolDetail td = jdbcTemplate.queryForObject(getQueryForBasicToolDetail(toolId), new RowMapper<ToolDetail>() {
            @Override
            public ToolDetail mapRow(ResultSet rs, int i) throws SQLException {
                ToolDetail td = new ToolDetail();
                td.setToolType(rs.getString("type"));
                td.setToolSubType(rs.getString("sub_type"));
                td.setToolPowerSource(rs.getString("power_source"));
                td.setShortDescription(rs.getString("description"));
                td.setFullDescription(rs.getString("detail"));
                td.setDepositPrice(rs.getDouble("deposit_price"));
                td.setRentalPrice(rs.getDouble("rental_price"));
                return td;
            }
        });
        td.setToolId(toolId);
        String toolSubTable = toolSubTypeTableNameMap.get(td.getToolSubType());
        if (toolSubTable != null) {
            String specificationQuery = getQueryForSpecificToolDetail(toolId, toolSubTable);
            String specification = jdbcTemplate.queryForObject(specificationQuery, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet rs, int i) throws SQLException {
                    return rs.getString("specification");
                }
            });
            td.setSpecificDescription(specification);
        }
        List<String> accessories = new ArrayList<>();
        if (toolSubTable != null && toolSubTable.contains("power")) {
            //power tool
            if ("cordless".equalsIgnoreCase(td.getToolPowerSource())) {
                accessories = getCordlessToolAccessoriesDetail(toolId);
            } else {
                accessories = getPowerToolAccessoriesDetail(toolId);
            }
        }
        td.setAccessories(accessories);
        return td;
    }

    private String getQueryForBasicToolDetail(int toolId) {
        return "SELECT \n" +
                " t.toolID, t.type, t.sub_type, t.power_source, CONCAT(IF(t.power_source = 'Manual','', CONCAT(t.power_source, ' ') \n" +
                "),t.sub_option,' ',t.sub_type) AS description, CONCAT(\n" +
                " t.width, \n" +
                " ' ', \n" +
                " t.width_unit, \n" +
                " ' W x ', \n" +
                " t.len, \n" +
                " ' ', \n" +
                " t.len_unit, \n" +
                " ' L ', \n" +
                " ' ', IF(t.power_source = 'Manual','', CONCAT(t.power_source,' ',pt.volt_rate,' ',pt.volt_rate_unit,' ',pt.amp_rate,' ',pt.amp_rate_unit,' ',pt.min_rpm_rate,'min-rpm ', IF(pt.max_rpm_rate IS NULL,'', CONCAT(pt.max_rpm_rate, 'max-rpm ')))), \n" +
                "t.sub_option,' ',t.sub_type,' ', \n" +
                " ' by ',t.manufacturer \n" +
                ") AS detail, ROUND((t.price * .15),2) AS rental_price, ROUND((t.price * .40),2) AS deposit_price\n" +
                "FROM \n" +
                " Tool t\n" +
                "LEFT JOIN powertool pt ON pt.toolID=t.toolID\n" +
                "WHERE \n" +
                " t.toolID = " + toolId +";";
    }

    private String getQueryForSpecificToolDetail(int toolId, String toolSubType) {
        return ToolDetailEnum.valueOf(toolSubType.toUpperCase()).getQueryForSpecificToolDetail(toolId);
    }

    private String getQueryForPowerToolAccessories(int toolId) {
        return "SELECT acc_descript FROM poweraccessories WHERE tool_id = " + toolId + ";";
    }

    private List<String> getPowerToolAccessoriesDetail(int toolId) {
        String query = getQueryForPowerToolAccessories(toolId);
        return jdbcTemplate.query(query, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("acc_descript");
            }
        });
    }

    private List<String> getCordlessToolAccessoriesDetail(int toolId) {
        String query = getQueryForCordlessToolAccessories(toolId);
        return jdbcTemplate.query(query, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("accessories");
            }
        });
    }

    private String getQueryForCordlessToolAccessories(int toolId) {
        return "SELECT IF(c.battery_type IS NULL, p.acc_descript, c.battery_type) AS accessories " +
                "FROM poweraccessories p LEFT JOIN cordlessbattery c " +
                "ON p.accessoryID=c.accessoryID WHERE p.tool_id=" + toolId;
    }

    private static Map<String, String> toolTypeMap = new HashMap<>();
    private static Map<String, String> toolSubTypeTableNameMap = new HashMap<>();

    static {
        toolTypeMap.put("hand", "toolID");
        toolTypeMap.put("hand tool", "toolID");
        toolTypeMap.put("garden", "toolID");
        toolTypeMap.put("garden tool", "toolID");
        toolTypeMap.put("ladder", "toolID");
        toolTypeMap.put("ladder tool", "toolID");
        toolTypeMap.put("power", "powertoolID");
        toolTypeMap.put("power tool", "powertoolID");

        //hand
        toolSubTypeTableNameMap.put("screwdriver", "handscrewdriver");
        toolSubTypeTableNameMap.put("socket", "handsocket");
        toolSubTypeTableNameMap.put("ratchet", "handratchet");
        toolSubTypeTableNameMap.put("pliers", "handplier");
        toolSubTypeTableNameMap.put("gun", "handgun");
        toolSubTypeTableNameMap.put("hammer", "handhammer");
        //garden
        toolSubTypeTableNameMap.put("digger", "diggingtool");
        toolSubTypeTableNameMap.put("pruner", "prunningtool");
        toolSubTypeTableNameMap.put("rakes", "raketool");
        toolSubTypeTableNameMap.put("wheelbarrows", "wheelbarrow");
        toolSubTypeTableNameMap.put("striking", "strikingtool");
        //ladder
        toolSubTypeTableNameMap.put("straight", "straightladder");
        toolSubTypeTableNameMap.put("step", "stepladder");
        //power
        toolSubTypeTableNameMap.put("drill", "powerdrill");
        toolSubTypeTableNameMap.put("saw", "powersaw");
        toolSubTypeTableNameMap.put("sander", "powersander");
        toolSubTypeTableNameMap.put("aircompressor", "poweraircompressor");
        toolSubTypeTableNameMap.put("mixer", "powermixer");
        toolSubTypeTableNameMap.put("generator", "powergenerator");

    }
}
