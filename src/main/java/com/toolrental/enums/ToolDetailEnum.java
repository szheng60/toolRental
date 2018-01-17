package com.toolrental.enums;

/**
 * Created by xinyu on 11/4/2017.
 */
public enum ToolDetailEnum {
    HANDGUN {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('capacity - ', cap, ' ', cap_unit, IF(gauge_rating IS NULL, '', CONCAT('; gauge rating - ' , gauge_rating, ' ', gauge_unit))) " +
                    "AS specification FROM handgun WHERE toolID=" + toolId + ";";
        }
    }, HANDHAMMER {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('anti vibration - ', IF(anti_vibration=1, 'true', 'false')) AS specification FROM handhammer WHERE toolID=" + toolId + ";";
        }
    }, HANDPLIER {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('adjustable - ', IF(adj=1, 'true', 'false')) AS specification FROM handplier WHERE toolID=" + toolId + ";";
        }
    }, HANDRATCHET {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('drive size - ', drive_size, ' ', drive_size_unit) AS specification FROM handracket WHERE toolID=" + toolId + ";";
        }
    }, HANDSCREWDRIVER {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('screw size - #', screw_size) AS specification FROM handscrewdriver WHERE toolID=" + toolId + ";";
        }
    }, HANDSOCKET {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('drive size - ', drive_size, ' ', drive_size_unit, '; sae size - ', sae_size, ' ', sae_size_unit, \n" +
                    "IF(deep_socket IS NULL, '', CONCAT('; deep socket - ', IF(deep_socket=1, 'true', 'false'))))  AS specification FROM handsocket WHERE toolID=" + toolId + ";";
        }
    }, POWERAIRCOMPRESSOR {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('tank size - ', tank_size, ' ', tank_size_unit, IF(pressure_rate IS NULL, '', CONCAT('; pressure rating - ', pressure_rate, ' ', pressure_rate_unit))) " +
                    "AS specification FROM poweraircompressor WHERE powertoolID=" + toolId + ";";
        }
    },POWERDRILL {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('adjustable clutch - ', IF(adj_clutch=1, 'true', 'false'), '; min torque rating - ', min_torque_rate, ' ', min_torque_rate_unit, '', " +
                    "IF(max_torque_rate IS NULL, '', CONCAT('; max torque rating - ', max_torque_rate, ' ', max_torque_rate_unit))) " +
                    "AS specification FROM powerdrill WHERE powertoolID=" + toolId + ";";
        }
    },POWERGENERATOR {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('power rating - ', power_rate, ' ', power_rate_unit) AS specification FROM powergenerator WHERE powertoolID=" + toolId + ";";
        }
    },POWERMIXER {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('motor rating - ', motor_rate, ' ', motor_rate_unit, '; drum size - ', drum_size, ' ', drum_size_unit) " +
                    "AS specification FROM powermixer WHERE powertoolID=" + toolId + ";";
        }
    },POWERSANDER {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('dust bag - ', IF(dust_bag=1, 'true', 'false')) AS specification FROM powersander WHERE powertoolID=" + toolId + ";";
        }
    },
    POWERSAW {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('blade size - ', blade_size, ' ', blade_size_unit) AS specification FROM powersaw WHERE powertoolID=" + toolId + ";";
        }
    }, DIGGINGTOOL {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('handle material - ', handle_mat, '; blade length - ' , blade_len, ' ', blade_len_unit, \n" +
                    "IF(blade_width IS NULL, '', CONCAT('; blade width - ', blade_width, ' ', blade_width_unit))) " +
                    "AS specification FROM diggingtool WHERE toolID=" + toolId + ";";
        }
    }, PRUNNINGTOOL {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('handle material - ', handle_mat, '; blade length - ', blade_len, ' ', blade_len_unit, " +
                    "IF(blade_mat IS NULL, '', CONCAT('; blade material - ', blade_mat))) AS specification FROM pruningtool WHERE toolID=" + toolId + ";";
        }
    },RAKETOOL {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('handle material - ', handle_mat, '; tine count - ', tine_cnt) AS specification FROM raketool WHERE toolID=" + toolId + ";";
        }
    },STRIKINGTOOL {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('handle material - ', handle_mat, '; head weight - ', head_weight, ' ', head_weight_unit) " +
                    "AS specification FROM strikingtool WHERE toolID=" + toolId + ";";
        }
    },WHEELBARROW {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT('handle material - ', handle_mat, '; wheel count - ', wheel_cnt, ' wheeled; bin material - ', bin_mat, '', " +
                    "IF(bin_vol IS NULL, '', CONCAT('; bin volume - ', bin_vol, ' ', bin_vol_unit))) " +
                    "AS specification FROM wheelbarrow WHERE toolID=" + toolId + ";";
        }
    },STEPLADDER {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT(IF(step_cnt IS NULL, '', CONCAT('step count - ', step_cnt, '; ')), " +
                    "IF(weight_cap IS NULL, '', CONCAT('weight capacity - ', weight_cap, ' ', weight_cap_unit, '; ')), " +
                    "IF(pail_shelf IS NULL, '', CONCAT('pail shelf - ', IF(pail_shelf=0, 'true', 'false')))) " +
                    "AS specification FROM stepladder WHERE toolID=" + toolId + ";";
        }
    },STRAIGHTLADDER {
        @Override
        public String getQueryForSpecificToolDetail(int toolId) {
            return "SELECT CONCAT(IF(step_cnt IS NULL, '', CONCAT('step count - ', step_cnt, '; ')), " +
                    "IF(weight_cap IS NULL, '', CONCAT('weight capacity - ', weight_cap, ' ', weight_cap_unit, '; ')), " +
                    "IF(rub_feet IS NULL, '', CONCAT('rubber feet - ', IF(rub_feet=0, 'true', 'false')))) " +
                    "AS specification FROM straightladder WHERE toolID=" + toolId + ";";
        }
    };

    public abstract String getQueryForSpecificToolDetail(int toolId);
}
