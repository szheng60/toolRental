/**
 * Created by xinyu on 11/14/2017.
 */

$(document).ready(function() {
    var toolSubType = $('#tool_sub_type');
    var toolSubOption = $('#tool_sub_option');
    var toolPowerSource = $('#tool_power_source');
    var toolWidthFraction = $('#tool_width_fraction');
    var toolLengthFraction = $('#tool_length_fraction');
    var toolSpecification = $('#tool_specification');

    $('.tool-type').click(function(e) {
        var element = e.target;
        var toolValue = $(element).val();
        reset();
        if (toolValue == "Garden-Tool") {
            $('.garden-material-specification').removeClass('hide');
        } else if (toolValue == "Ladder-Tool") {
            $('.ladder-specification').removeClass('hide');
        } else if (toolValue == "Power-Tool") {
            $('#tool_power_source').find('.power_tool').removeClass('hide');
            $('.power-specification').removeClass('hide');
            $('.power-accessories-specification').removeClass('hide');
        }
        $.each(getToolSubType(toolValue), function(i, d) {
            toolSubType.prop('name', toolValue);
            toolSubType.append($("<option>").attr('value', d.val).text(d.text));
        });
    });

    $.each(getFractionOptions(), function(i, d) {
        $('.fraction-option').append($("<option>").attr('value', d.val).text(d.text));
    });

    $(':input').change(function() {
        $(this).closest('.form-group').removeClass('has-danger');
    })
    $('#confirmBtn').click(function() {
        var invalidEntries = 0
        $('input:invalid').each(function() {
            if ($(this).is(':visible')) {
                $(this).closest('.form-group').addClass('has-danger');
                invalidEntries++;
            }
        });
        $('select.required').each(function() {
            if ($(this).val() == 0 && $(this).is(':visible')) {
                $(this).parent().addClass('has-danger');
                invalidEntries++;
            }
        })

        if (invalidEntries == 0) {
            proceedToAddTool();
        }
    })

    toolSubType.change(function(e) {
        var subType = $(this).val();
        var toolType = $('.tool-type:checked').val();
        toolSpecification.find('.specification').addClass('hide');
        if (toolType == 'Power-Tool') {
            $('.power-specification').removeClass('hide');
            $('.power-accessories-specification').removeClass('hide');
        }
        if (subType != 0) {
            toolSpecification.find('.'+subType+'-specification').removeClass('hide');
            switch(toolType) {
                case "Hand-Tool":
                    return getToolSubOptions(subType, getHandToolSubOptionForSubTypes);
                case "Garden-Tool":
                    $('.garden-material-specification').removeClass('hide');
                    return getToolSubOptions(subType, getGardenToolSubOptionForSubTypes);
                case "Ladder-Tool":
                    $('.ladder-specification').removeClass('hide');
                    return getToolSubOptions(subType, getLadderToolSubOptionForSubTypes);
                case "Power-Tool":
                    $('.power-specification').removeClass('hide');
                    $('.power-accessories-specification').removeClass('hide');
                    return getToolSubOptions(subType, getPowerToolSubOptionForSubTypes);
            }
        }
    });

    $('#power_tool_specification').on('change', '#power_accessories_select', function() {
        var accessoriesRow = $(this).closest('.power-accessories-row');
        accessoriesRow.find('.power_accessories_specific').addClass('hide');
        var accessory = $(this).val();
        if (accessory == 'safety') {
            accessoriesRow.find('.safe').removeClass('hide');
        }
    })

    $('#power_tool_specification').on('click', '.remove-accessory', function() {
        var currentRow = $(this).closest('.power-accessories-row');
        if (currentRow.index() != 0) {
            currentRow.remove();
        }
    })
    $('#power_tool_specification').on('click', '.add-accessory', function() {
        var currentRow = $(this).closest('.power-accessories-row');
        var newRow = currentRow.clone();
        newRow.find('.accessories-select').val('drillbits');
        currentRow.parent().append(newRow);
    })

    toolPowerSource.change(function() {
        var ps = $(this).val();
        if (ps == 'cordless') {
            $('.cordless-accessories-row').removeClass('hide');
        } else {
            $('.cordless-accessories-row').addClass('hide');
        }
    })

    function reset() {
        toolSubType.empty();
        toolSubType.append($("<option>").attr('value', 0).text("--Select One--"));
        toolSubOption.empty();
        toolSubOption.append($("<option>").attr('value', 0).text("--Select One--"));
        toolSpecification.find('.specification').addClass('hide');
        $('#tool_power_source').find('.power_tool').addClass('hide');
    }

    function getToolSubOptions(subType, callback) {
        toolSubOption.empty();
        toolSubOption.append($("<option>").attr('value', 0).text("--Select One--"));
        $.each(callback(subType), function(i, d) {
            toolSubOption.prop('name', subType);
            toolSubOption.append($("<option>").attr('value', d.val).text(d.text));
        });
    }

    function getToolSubType(toolValue) {
        switch(toolValue) {
            case "Hand-Tool":
                return getHandToolSubTypes();
            case "Garden-Tool":
                return getGardenToolSubTypes();
            case "Ladder-Tool":
                return getLadderToolSubTypes();
            case "Power-Tool":
                return getPowerToolSubTypes();
        }
    }
    function getHandToolSubTypes() {
        return [
            {val: 'screwdriver', text: 'Screwdriver'},
            {val: 'socket', text: 'Socket'},
            {val: 'ratchet', text: 'Ratchet'},
            {val: 'wrench', text: 'Wrench'},
            {val: 'pliers', text: 'Pliers'},
            {val: 'gun', text: 'Gun'},
            {val: 'hammer', text: 'Hammer'}
        ];
    }
    function getGardenToolSubTypes() {
        return [
            {val: 'digger', text: 'Digger'},
            {val: 'pruner', text: 'Pruner'},
            {val: 'rakes', text: 'Rakes'},
            {val: 'wheelbarrows', text: 'Wheelbarrows'},
            {val: 'striking', text: 'Striking'}
        ]
    }
    function getLadderToolSubTypes() {
        return [
            {val: 'straight', text: 'Straight'},
            {val: 'step', text: 'Step'}
        ]
    }
    function getPowerToolSubTypes() {
        return [
            {val: 'drill', text: 'Drill'},
            {val: 'saw', text: 'Saw'},
            {val: 'sander', text: 'Sander'},
            {val: 'aircompressor', text: 'Air-Compressor'},
            {val: 'mixer', text: 'Mixer'},
            {val: 'generator', text: 'Generator'}
        ]
    }
    function getHandToolSubOptionForSubTypes(subType) {
        switch(subType) {
            case "screwdriver":
                return getHandToolSubOptionForScrewdriver();
            case "socket":
                return getHandToolSubOptionForSocket();
            case "ratchet":
                return getHandToolSubOptionForRatchet();
            case "wrench":
                return getHandToolSubOptionForWrench();
            case "pliers":
                return getHandToolSubOptionForPliers();
            case "gun":
                return getHandToolSubOptionForGun();
            case "hammer":
                return getHandToolSubOptionForHammer();
        }
    }
    function getGardenToolSubOptionForSubTypes(subType) {
        switch(subType) {
            case "digger":
                return getGardenToolSubOptionForDigger();
            case "pruner":
                return getGardenToolSubOptionForPruner();
            case "rakes":
                return getGardenToolSubOptionForRakes();
            case "wheelbarrows":
                return getGardenToolSubOptionForWheelbarrows();
            case "striking":
                return getGardenToolSubOptionForStriking();
        }
    }
    function getLadderToolSubOptionForSubTypes(subType) {
        switch(subType) {
            case "straight":
                return getLadderToolSubOptionForStraight();
            case "step":
                return getLadderToolSubOptionForStep();
        }
    }
    function getPowerToolSubOptionForSubTypes(subType) {
        switch(subType) {
            case "drill":
                return getPowerToolSubOptionForDrill();
            case "saw":
                return getPowerToolSubOptionForSaw();
            case "sander":
                return getPowerToolSubOptionForSander();
            case "aircompressor":
                return getPowerToolSubOptionForAirCompressor();
            case "mixer":
                return getPowerToolSubOptionForMixer();
            case "generator":
                return getPowerToolSubOptionForGenerator();
        }
    }
    function getHandToolSubOptionForScrewdriver() {
        return [
            {val: 'phillips', text: 'phillips'},
            {val: 'hex', text: 'hex'},
            {val: 'torx', text: 'torx'},
            {val: 'slotted', text: 'slotted'}
        ]
    }
    function getHandToolSubOptionForSocket() {
        return [
            {val: 'deep', text: 'deep'},
            {val: 'standard', text: 'standard'},
        ]
    }
    function getHandToolSubOptionForRatchet() {
        return [
            {val: 'adjustable', text: 'adjustable'},
            {val: 'fixed', text: 'fixed'},
        ]
    }
    function getHandToolSubOptionForWrench() {
        return [
            {val: 'crescent', text: 'crescent'},
            {val: 'torque', text: 'torque'},
            {val: 'pipe', text: 'pipe'},
        ]
    }
    function getHandToolSubOptionForPliers() {
        return [
            {val: 'needlenose', text: 'needle nose'},
            {val: 'cutting', text: 'cutting'},
            {val: 'crimper', text: 'crimper'},
        ]
    }
    function getHandToolSubOptionForGun() {
        return [
            {val: 'nail', text: 'nail'},
            {val: 'staple', text: 'staple'},
        ]
    }
    function getHandToolSubOptionForHammer() {
        return [
            {val: 'claw', text: 'claw'},
            {val: 'sledge', text: 'sledge'},
            {val: 'framing', text: 'framing'},
        ]
    }
    function getGardenToolSubOptionForDigger() {
        return [
            {val: 'pointedshovel', text: 'pointed shovel'},
            {val: 'flatshovel', text: 'flat shovel'},
            {val: 'scoopshovel', text: 'scoop shovel'},
            {val: 'edger', text: 'edger'}
        ]
    }
    function getGardenToolSubOptionForPruner() {
        return [
            {val: 'sheer', text: 'sheer'},
            {val: 'loppers', text: 'loppers'},
            {val: 'hedge', text: 'hedge'},
        ]
    }
    function getGardenToolSubOptionForRakes() {
        return [
            {val: 'leaf', text: 'leaf'},
            {val: 'landscaping', text: 'landscaping'},
            {val: 'rock', text: 'rock'},
        ]
    }
    function getGardenToolSubOptionForWheelbarrows() {
        return [
            {val: '1wheel', text: '1-wheel'},
            {val: '2wheel', text: '2-wheel'}
        ]
    }
    function getGardenToolSubOptionForStriking() {
        return [
            {val: 'barpry', text: 'bar pry'},
            {val: 'rubbermallet', text: 'rubber mallet'},
            {val: 'tamper', text: 'tamper'},
            {val: 'pickaxe', text: 'pick axe'},
            {val: 'singlebitaxe', text: 'single bit axe'}
        ]
    }
    function getLadderToolSubOptionForStraight() {
        return [
            {val: 'rigid', text: 'rigid'},
            {val: 'telescoping', text: 'telescoping'}
        ]
    }
    function getLadderToolSubOptionForStep() {
        return [
            {val: 'folding', text: 'folding'},
            {val: 'multiposition', text: 'multi-position'}
        ]
    }
    function getPowerToolSubOptionForDrill() {
        return [
            {val: 'driver', text: 'driver'},
            {val: 'hammer', text: 'hammer'}
        ]
    }
    function getPowerToolSubOptionForSaw() {
        return [
            {val: 'circular', text: 'circular'},
            {val: 'reciprocating', text: 'reciprocating'},
            {val: 'jig', text: 'jig'},
        ]
    }
    function getPowerToolSubOptionForSander() {
        return [
            {val: 'finish', text: 'finish'},
            {val: 'sheet', text: 'sheet'},
            {val: 'belt', text: 'belt'},
            {val: 'randomorbital', text: 'random orbital'}
        ]
    }
    function getPowerToolSubOptionForAirCompressor() {
        return [
            {val: 'reciprocating', text: 'reciprocating'}
        ]
    }
    function getPowerToolSubOptionForMixer() {
        return [
            {val: 'concrete', text: 'concrete'}
        ]
    }
    function getPowerToolSubOptionForGenerator() {
        return [
            {val: 'electric', text: 'electric'}
        ]
    }
    function getFractionOptions() {
        return [
            {val: '0.0625', text: '1/16'},
            {val: '0.125', text: '1/8'},
            {val: '0.1875', text: '3/16'},
            {val: '0.25', text: '1/4'},
            {val: '0.3125', text: '5/16'},
            {val: '0.375', text: '3/8'},
            {val: '0.4375', text: '7/16'},
            {val: '0.5', text: '1/2'},
            {val: '0.5625', text: '9/16'},
            {val: '0.625', text: '5/8'},
            {val: '0.6875', text: '11/16'},
            {val: '0.75', text: '3/4'},
            {val: '0.8125', text: '13/16'},
            {val: '0.875', text: '7/8'},
            {val: '0.9375', text: '15/16'}
        ]
    }

    function proceedToAddTool() {
        var toolType = $('input[name=tool-type]:checked').val();
        var toolSubType = $('#tool_sub_type').val();
        var toolSubOption = $('#tool_sub_option').val();
        var toolPowerSource = $('#tool_power_source').val();
        var toolPurchasePrice = $('#tool_purchase_price').val();
        var toolManufacture = $('#tool_manufacturer').val();
        var toolMaterial = $('#tool_material').val();
        var toolWidth = $('#tool_width').val();
        var toolWidthFraction = $('#tool_width_fraction').val();
        var toolWidthUnit = $('#tool_width_unit').val();
        var toolWeight = $('#tool_weight').val();
        var toolLength = $('#tool_length').val();
        var toolLengthFraction = $('#tool_length_fraction').val();
        var toolLengthUnit = $('#tool_length_unit').val();

        //Hand Tool
        var screwdriverScrewSize = $('#screwdriver_screw_size').val();
        var socketDriveSize = $('#socket_drive_size').val();
        var socketSaeSize = $('#socket_sae_size').val();
        var socketDeepSocket = $('#socket_deep_socket').val();
        var ratchetDriveSize = $('#ratchet_drive_size').val();
        var pliersAdjustable = $('#pliers_adjustable').val();
        var gunGaugeRating = $('#gun_gauge_rating').val();
        var gunCapacity = $('#gun_capacity').val();
        var hammerAntiVibratioin = $('#hammer_anti_vibration').val();

        //Garden Tool
        var gardenHandleMaterial = $('#garden_handle_material').val();
        var prunerBladeMaterial = $('#pruner_blade_material').val();
        var prunerBladeLength = $('#pruner_blade_length').val();
        var prunerBladeLengthFraction = $('#pruner_blade_length_fraction').val();
        var strikingHeadWeight = $('#striking_head_weight').val();
        var diggerBladeWidth = $('#digger_blade_width').val();
        var diggerBladeWidthFraction = $('#digger_blade_width_fraction').val();
        var diggerBladeLength = $('#digger_blade_length').val();
        var diggerBladeLengthFraction = $('#digger_blade_length_fraction').val();
        var rakesTineCount = $('#rakes_tine_count').val();
        var wheelbarrowsBinMaterial = $('#wheelbarrows_bin_material').val();
        var wheelbarrowsBinVolume = $('#wheelbarrows_bin_volume').val();
        var wheelbarrowsWheelCount = $('#wheelbarrows_wheel_count').val();

        //Ladder Tool
        var ladderStepCount = $('#ladder_step_count').val();
        var ladderWeightCapacity = $('#ladder_weight_capacity').val();
        var straightLadderRubberFeet = $('#straight_ladder_rubber_feet').val();
        var stepLadderPailShelf = $('#step_ladder_pail_shelf').val();

        //Power Tool
        var powerVoltRating = $('#power_volt_rating').val();
        var powerAmpRating = $('#power_amp_rating').val();
        var powerMinRpmRating = $('#power_min_rpm_rating').val();
        var powerMaxRpmRating = $('#power_max_rpm_rating').val();
        var drillAdjustableClutch = $('#drill_adjustable_clutch').val();
        var drillMinTorqueRating = $('#drill_min_torque_rating').val();
        var drillMaxTorqueRating = $('#drill_max_torque_rating').val();
        var sawBladeSize = $('#saw_blade_size').val();
        var sawBladeSizeFraction = $('#saw_blade_size_fraction').val();
        var sanderDustBag = $('#sander_dust_bag').val();
        var aircompressorTankSize = $('#aircompressor_tank_size').val();
        var aidcompressorPressureRating = $('#aircompressor_pressure_rating').val();
        var mixerMotorRating = $('#mixer_motor_rating').val();
        var mixerMotorRatingFraction = $('#mixer_motor_rating_fraction').val();
        var mixerDrumSize = $('#mixer_drum_size').val();
        var generatorPowerRating = $('#generator_power_rating').val();

        //Accessories
        var powerAccessoriesSelect = $('#power_accessories_select').val();
        var safetyOption = $('#safety_select').val();
        var accessoriesQuantity = $('#accessories_quantity').val();
        var batteryType = $('#battery_type').val();
        var batteryRating = $('#battery_rating').val();
        var batteryQuantity = $('#battery_quantity').val();

        var formData = {};
        formData["toolType"]=toolType;
        formData["toolSubType"]=toolSubType
        formData["toolSubOption"]=toolSubOption
        formData["toolPowerSource"]=toolPowerSource
        formData["toolPurchasePrice"]=toolPurchasePrice
        formData["toolManufacture"]=toolManufacture
        formData["toolMaterial"]=toolMaterial
        formData["toolWidth"]=toolWidth
        formData["toolWidthFraction"]=toolWidthFraction
        formData["toolWidthUnit"]=toolWidthUnit
        formData["toolWeight"]=toolWeight
        formData["toolLength"]=toolLength
        formData["toolLengthFraction"]=toolLengthFraction
        formData["toolLengthUnit"]=toolLengthUnit

        //Hand Tool
        formData["screwdriverScrewSize"]=screwdriverScrewSize
        formData["socketDriveSize"]=socketDriveSize
        formData["socketSaeSize"]=socketSaeSize
        formData["socketDeepSocket"]=socketDeepSocket
        formData["ratchetDriveSize"]=ratchetDriveSize
        formData["pliersAdjustable"]=pliersAdjustable
        formData["gunGaugeRating"]=gunGaugeRating
        formData["gunCapacity"]=gunCapacity
        formData["hammerAntiVibratioin"]=hammerAntiVibratioin

        //Garden Tool
        formData["gardenHandleMaterial"]=gardenHandleMaterial
        formData["prunerBladeMaterial"]=prunerBladeMaterial
        formData["prunerBladeLength"]=prunerBladeLength
        formData["prunerBladeLengthFraction"]=prunerBladeLengthFraction
        formData["strikingHeadWeight"]=strikingHeadWeight
        formData["diggerBladeWidth"]=diggerBladeWidth
        formData["diggerBladeWidthFraction"]=diggerBladeWidthFraction
        formData["diggerBladeLength"] = diggerBladeLength
        formData["diggerBladeLengthFraction"] = diggerBladeLengthFraction
        formData["rakesTineCount"]=rakesTineCount
        formData["wheelbarrowsBinMaterial"]=wheelbarrowsBinMaterial
        formData["wheelbarrowsBinVolume"]=wheelbarrowsBinVolume
        formData["wheelbarrowsWheelCount"]=wheelbarrowsWheelCount

        //Ladder Tool
        formData["ladderStepCount"]=ladderStepCount
        formData["ladderWeightCapacity"]=ladderWeightCapacity
        formData["straightLadderRubberFeet"]=straightLadderRubberFeet
        formData["stepLadderPailShelf"]=stepLadderPailShelf

        //Power Tool
        formData["powerVoltRating"]=powerVoltRating
        formData["powerAmpRating"]=powerAmpRating
        formData["powerMinRpmRating"]=powerMinRpmRating
        formData["powerMaxRpmRating"]=powerMaxRpmRating
        formData["drillAdjustableClutch"]=drillAdjustableClutch
        formData["drillMinTorqueRating"]=drillMinTorqueRating
        formData["drillMaxTorqueRating"]=drillMaxTorqueRating
        formData["sawBladeSize"]=sawBladeSize
        formData["sawBladeSizeFraction"]=sawBladeSizeFraction
        formData["sanderDustBag"]=sanderDustBag
        formData["aircompressorTankSize"]=aircompressorTankSize
        formData["aidcompressorPressureRating"]=aidcompressorPressureRating
        formData["mixerMotorRating"]=mixerMotorRating
        formData["mixerMotorRatingFraction"]=mixerMotorRatingFraction
        formData["mixerDrumSize"]=mixerDrumSize
        formData["generatorPowerRating"]=generatorPowerRating

        //Accessories
        formData["powerAccessoriesSelect"]=powerAccessoriesSelect
        formData["safetyOption"]=safetyOption
        formData["accessoriesQuantity"]=accessoriesQuantity
        formData["batteryType"]=batteryType
        formData["batteryRating"]=batteryRating
        formData["batteryQuantity"]=batteryQuantity

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/api/addtool",
            data: JSON.stringify(formData),
            dataType: 'json',
            success: function(data) {
                alert("new tool added");
                var next = document.getElementById("nextUrl");
                next.href = "/addnewtool";
                next.click();
            },
            error: function(e) {
                alert("failed to add");
            }
        })
    }
})
