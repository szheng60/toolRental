package com.toolrental.dao;

import com.toolrental.form.AddToolForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xinyu on 11/17/2017.
 */
@Repository
public class AddToolDao implements IAddTool {
    private static Map<String, String> toolTypeMap = new HashMap<>();

    static {
        toolTypeMap.put("Hand-Tool", "toolID");
        toolTypeMap.put("Garden-Tool", "toolID");
        toolTypeMap.put("Ladder-Tool", "toolID");
        toolTypeMap.put("Power-Tool", "powertoolID");
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int addTool(final AddToolForm addToolForm) {
        KeyHolder holder = new GeneratedKeyHolder();
        PreparedStatementCreator creator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement addTool = con.prepareStatement(getQueryForAddTool(addToolForm), new String[] {toolTypeMap.get(addToolForm.getToolType())});
                return addTool;
            }
        };
        jdbcTemplate.update(creator, holder);
        int toolId = holder.getKey().intValue();

        return toolId;
    }

    @Override
    public boolean addSpecificTool(final AddToolForm addToolForm, final int toolId) {
        if (addToolForm.getToolType().equalsIgnoreCase("Power-Tool")) {
            jdbcTemplate.update(getQueryForPowerTool(addToolForm, toolId));
        }
        KeyHolder holder = new GeneratedKeyHolder();
        String queryForSpecificTool = getQueryForSpecificTool(addToolForm, toolId);
        jdbcTemplate.update(queryForSpecificTool);
        //add accessory
        if (addToolForm.getToolType().equalsIgnoreCase("Power-Tool")) {
            if (addToolForm.getToolPowerSource().equalsIgnoreCase("cordless")) {
                PreparedStatementCreator addCordless = new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement addBattery = con.prepareStatement(getQueryForPowerAccessories(addToolForm, toolId, true), new String[] {"accessoryID"});
                        return addBattery;
                    }
                };
                jdbcTemplate.update(addCordless, holder);
                int accessoryId = holder.getKey().intValue();
                jdbcTemplate.update(getQueryForBattery(addToolForm, toolId, accessoryId));
            } else {
                jdbcTemplate.update(getQueryForPowerAccessories(addToolForm, toolId, false));
            }
        }
        return true;
    }

    @Override
    public int removeTool(int toolId) {
        String query = "DELETE FROM tool where toolID=" + toolId;
        return jdbcTemplate.update(query);
    }

    private String getQueryForSpecificTool(AddToolForm addToolForm, int toolId) {
        ToolTableQueryHelper helper = new ToolTableQueryHelper(addToolForm, toolId);
        return helper.getQuery();
    }



    private String getQueryForAddTool(AddToolForm addToolForm) {
        String material = addToolForm.getToolMaterial();
        if (material.length()==0) {
            material = null;
        }
        return "INSERT INTO Tool(type,sub_type,sub_option,width,width_unit,weight,weight_unit,len,len_unit,manufacturer,material,price,power_source) VALUES('" +
                addToolForm.getToolType().replace("-", " ") + "','" +
                addToolForm.getToolSubType() + "','" +
                addToolForm.getToolSubOption() + "'," +
                reformatFraction(addToolForm.getToolWidth(), addToolForm.getToolWidthFraction()) + ",'" +
                addToolForm.getToolWidthUnit() + "'," +
                addToolForm.getToolWeight() + ", 'lb', " +
                reformatFraction(addToolForm.getToolLength(), addToolForm.getToolLengthFraction()) + ",'" +
                addToolForm.getToolLengthUnit() + "','" +
                addToolForm.getToolManufacture() + "','" +
                material + "'," +
                addToolForm.getToolPurchasePrice() + ",'" +
                addToolForm.getToolPowerSource()
                + "');";
                //"$type','$subType','$subOption','$width','$widthUnit','$weight','$weightUnit','$len','$lenUnit','$manufacturer','$material','$price','$powerSource');";
    }

    private String getQueryForScrewdriver(AddToolForm addToolForm, int toolId) {
        return "INSERT INTO handscrewdriver VALUES(" + toolId + "," + addToolForm.getScrewdriverScrewSize() + ");";
    }
    private String getQueryForSocket(AddToolForm addToolForm, int toolId) {
        return "INSERT INTO handsocket VALUES(" + toolId + "," +
                addToolForm.getSocketDriveSize() + ", 'in', " +
                addToolForm.getSocketSaeSize() + ", 'in', " +
                convertBoolean(addToolForm.getSocketDeepSocket()) + ");";
    }
    private String getQueryForRatchet(AddToolForm addToolForm, int toolId) {
        return "INSERT INTO handracket VALUES(" + toolId + "," +
                addToolForm.getRatchetDriveSize() + ", 'in');";
    }
    private String getQueryForPliers(AddToolForm addToolForm, int toolId) {
        return "INSERT INTO handplier VALUES(" + toolId + "," +
                convertBoolean(addToolForm.getPliersAdjustable()) + ");";
    }
    private String getQueryForGun(AddToolForm addToolForm, int toolId) {
        String gaugeRating = convertEmpty(addToolForm.getGunGaugeRating());
        return "INSERT INTO handgun VALUES(" + toolId + "," +
                gaugeRating + ", 'G', " +
                addToolForm.getGunCapacity() + ", 'staple');";
    }
    private String getQueryForHammer(AddToolForm addToolForm, int toolId) {
        String antiVibration = addToolForm.getHammerAntiVibratioin();
        return "INSERT INTO handhammer VALUES(" + toolId + "," +
                convertBoolean(antiVibration) + ");";
    }

    private String getQueryForPruner(AddToolForm addToolForm, int toolId) {
        String bladeMaterial = convertEmpty(addToolForm.getPrunerBladeMaterial());
        return "INSERT INTO pruningtool VALUES(" + toolId + ",'" + addToolForm.getGardenHandleMaterial() + "'," +
                bladeMaterial + "," +
                reformatFraction(addToolForm.getPrunerBladeLength(), addToolForm.getPrunerBladeLengthFraction()) + ", 'in');";
    }

    private String getQueryForStriking(AddToolForm addToolForm, int toolId) {
        return "INSERT INTO strikingtool VALUES(" + toolId + ",'" + addToolForm.getGardenHandleMaterial() + "'," +
                addToolForm.getStrikingHeadWeight() + ",'lb');";
    }

    private String getQueryForDigger(AddToolForm addToolForm, int toolId) {
        String bladeWidthUnit = "'in'";
        Double bladeWidth = reformatFraction(addToolForm.getDiggerBladeWidth(), addToolForm.getDiggerBladeWidthFraction());
        if (bladeWidth == 0) {
            bladeWidth = null;
            bladeWidthUnit = null;
        }
        return "INSERT INTO diggingtool VALUES(" + toolId + ",'" + addToolForm.getGardenHandleMaterial() + "'," +
                bladeWidth + "," + bladeWidthUnit + "," +
                reformatFraction(addToolForm.getDiggerBladeLength(), addToolForm.getDiggerBladeLengthFraction()) + ", 'in');";
    }

    private String getQueryForRakes(AddToolForm addToolForm, int toolId) {
        return "INSERT INTO raketool VALUES(" + toolId + ",'" + addToolForm.getGardenHandleMaterial() + "'," +
                addToolForm.getRakesTineCount() + ");";
    }

    private String getQueryForWheelBarrows(AddToolForm addToolForm, int toolId) {
        String binVolumeUnit = "'cu ft.'";
        String binVolume = addToolForm.getWheelbarrowsBinVolume();
        if (binVolume.length() == 0) {
            binVolume = null;
            binVolumeUnit = null;
        }
        return "INSERT INTO wheelbarrow VALUES(" + toolId + ",'" + addToolForm.getGardenHandleMaterial() + "'," +
                addToolForm.getWheelbarrowsWheelCount() + ",'" +
                addToolForm.getWheelbarrowsBinMaterial() + "'," +
                binVolume + "," + binVolumeUnit + ");";
    }
    private String getQueryForStraightLadder(AddToolForm addToolForm, int toolId) {
        String stepCount = convertEmpty(addToolForm.getLadderStepCount());
        String weightCapacityUnit = null;
        String weightCapacity = convertEmpty(addToolForm.getLadderWeightCapacity());
        if (weightCapacity!=null) {
            weightCapacityUnit = "'lb'";
        }
        return "INSERT INTO straightladder VALUES(" + toolId + "," +
                stepCount + "," + weightCapacity + "," + weightCapacityUnit + "," + convertBoolean(addToolForm.getStraightLadderRubberFeet()) + ");";
    }
    private String getQueryForStepLadder(AddToolForm addToolForm, int toolId) {
        String stepCount = convertEmpty(addToolForm.getLadderStepCount());
        String weightCapacityUnit = null;
        String weightCapacity = convertEmpty(addToolForm.getLadderWeightCapacity());
        if (weightCapacity!=null) {
            weightCapacityUnit = "'lb'";
        }
        return "INSERT INTO stepladder VALUES(" + toolId + "," +
                stepCount + "," + weightCapacity + "," + weightCapacityUnit + "," + convertBoolean(addToolForm.getStepLadderPailShelf()) + ");";
    }
    private String getQueryForPowerTool(AddToolForm addToolForm, int toolId) {
        String maxRpmRating = convertEmpty(addToolForm.getPowerMaxRpmRating());
        return "INSERT INTO powertool VALUES(" + toolId + "," +
                addToolForm.getPowerAmpRating() + ", 'Amp', " +
                addToolForm.getPowerVoltRating() + ", 'Volt', " +
                addToolForm.getPowerMinRpmRating() + "," +
                maxRpmRating + ");";
    }

    private String getQueryForDrill(AddToolForm addToolForm, int toolId) {
        String maxTorqueRatingUnit = null;
        String maxTorqueRating = convertEmpty(addToolForm.getDrillMaxTorqueRating());
        if (maxTorqueRating != null) {
            maxTorqueRatingUnit = "'ft-lb'";
        }
        return "INSERT INTO powerdrill VALUES(" + toolId + "," +
                convertBoolean(addToolForm.getDrillAdjustableClutch()) + "," +
                addToolForm.getDrillMinTorqueRating() + ", 'ft-lb', " +
                maxTorqueRating + "," + maxTorqueRatingUnit + ");";
    }
    private String getQueryForSaw(AddToolForm addToolForm, int toolId) {
        return "INSERT INTO powersaw VALUES(" + toolId + "," +
                reformatFraction(addToolForm.getSawBladeSize(), addToolForm.getSawBladeSizeFraction()) + ", 'in');";
    }
    private String getQueryForSander(AddToolForm addToolForm, int toolId) {
        return "INSERT INTO powersander VALUES(" + toolId + "," +
                convertBoolean(addToolForm.getSanderDustBag()) + ");";
    }
    private String getQueryForAirCompressor(AddToolForm addToolForm, int toolId) {
        String pressureRatingUnit = null;
        String pressureRating = convertEmpty(addToolForm.getAidcompressorPressureRating());
        if (pressureRating != null) {
            pressureRatingUnit = "'psi'";
        }
        return "INSERT INTO poweraircompressor VALUES(" + toolId + "," +
                addToolForm.getAircompressorTankSize() + ", 'gallon', " +
                pressureRating + "," + pressureRatingUnit + ");";
    }
    private String getQueryForMixer(AddToolForm addToolForm, int toolId) {
        return "INSERT INTO powermixer VALUES(" + toolId + "," +
                addToolForm.getMixerDrumSize() + ", 'cu ft.', " +
                reformatFraction(addToolForm.getMixerMotorRating(), addToolForm.getMixerMotorRatingFraction()) + ", 'HP');";
    }
    private String getQueryForGenerator(AddToolForm addToolForm, int toolId) {
        return "INSERT INTO powergenerator VALUES(" + toolId + "," +
                addToolForm.getGeneratorPowerRating() + ", 'Watts');";
    }

    private String getQueryForPowerAccessories(AddToolForm addToolForm, int toolId, boolean isCordless) {
        String accessory = null;
        if (isCordless) {
            accessory = "battery";
        } else {
            accessory = addToolForm.getPowerAccessoriesSelect();
            if (accessory.equalsIgnoreCase("safety")) {
                accessory = accessory + " - " + addToolForm.getSafetyOption();
            }
        }
        return "INSERT INTO poweraccessories(tool_id, acc_descript) VALUES(" + toolId + ",'" + accessory + "');";
    }

    private String getQueryForBattery(AddToolForm addToolForm, int toolId, int accessoryId) {
        String desc = addToolForm.getBatteryRating() + "V " + addToolForm.getBatteryType() + " Battery";
        return "INSERT INTO cordlessbattery VALUES(" + accessoryId + ", '" + desc + "');";
    }

    private String convertEmpty(String input) {
        if (input == null) {
            return null;
        }
        if (input.length() == 0) {
            return null;
        }
        return "'" + input + "'";
    }
    private Integer convertBoolean(String input) {
        switch(input) {
            case "true":
                return 1;
            case "false":
                return 0;
        }
        return null;
    }

    private double reformatFraction(String wholeNumber, String fraction) {
        if (StringUtils.isEmpty(wholeNumber)) {
            return Double.valueOf(fraction);
        }
        return Double.valueOf(wholeNumber) + Double.valueOf(fraction);
    }

    private class ToolTableQueryHelper {
        private int toolId;
        private AddToolForm addToolForm;
        private String query;

        ToolTableQueryHelper(AddToolForm addToolForm, int toolId) {
            this.toolId = toolId;
            this.addToolForm = addToolForm;
        }

        String getQuery() {
            switch(this.addToolForm.getToolType()) {
                case "Hand-Tool":
                    return getQueryFromHandTool();
                case "Garden-Tool":
                    return getQueryFromGardenTool();
                case "Ladder-Tool":
                    return getQueryFromLadderTool();
                case "Power-Tool":
                    return getQueryFromPowerTool();
            }
            return null;
        }

        private String getQueryFromHandTool() {
            switch(this.addToolForm.getToolSubType()) {
                case "screwdriver":
                    return getQueryForScrewdriver(this.addToolForm, this.toolId);
                case "socket":
                    return getQueryForSocket(this.addToolForm, this.toolId);
                case "ratchet":
                    return getQueryForRatchet(this.addToolForm, this.toolId);
                case "pliers":
                    return getQueryForPliers(this.addToolForm, this.toolId);
                case "gun":
                    return getQueryForGun(this.addToolForm, this.toolId);
                case "hammer":
                    return getQueryForHammer(this.addToolForm, this.toolId);
                case "wrench":
                    return "no-table";
            }
            return null;
        }
        private String getQueryFromGardenTool() {
            switch(this.addToolForm.getToolSubType()) {
                case "digger":
                    return getQueryForDigger(this.addToolForm, this.toolId);
                case "pruner":
                    return getQueryForPruner(this.addToolForm, this.toolId);
                case "rakes":
                    return getQueryForRakes(this.addToolForm, this.toolId);
                case "wheelbarrows":
                    return getQueryForWheelBarrows(this.addToolForm, this.toolId);
                case "striking":
                    return getQueryForStriking(this.addToolForm, this.toolId);
            }
            return null;
        }
        private String getQueryFromLadderTool() {
            switch(this.addToolForm.getToolSubType()) {
                case "straight":
                    return getQueryForStraightLadder(this.addToolForm, this.toolId);
                case "step":
                    return getQueryForStepLadder(this.addToolForm, this.toolId);
            }
            return null;
        }
        private String getQueryFromPowerTool() {
            switch(this.addToolForm.getToolSubType()) {
                case "drill":
                    return getQueryForDrill(this.addToolForm, this.toolId);
                case "saw":
                    return getQueryForSaw(this.addToolForm, this.toolId);
                case "sander":
                    return getQueryForSander(this.addToolForm, this.toolId);
                case "aircompressor":
                    return getQueryForAirCompressor(this.addToolForm, this.toolId);
                case "mixer":
                    return getQueryForMixer(this.addToolForm, this.toolId);
                case "generator":
                    return getQueryForGenerator(this.addToolForm, this.toolId);
            }
            return null;
        }
    }
}
