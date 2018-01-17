$(document).ready(function() {
    // var dateControl = document.querySelector('input[type="Date"]');
    // dateControl.value = '2017-06-01';
    var toolTable = $('#content');
    var toolCart = $('#toolCart');
    var toolType = $('#tool_type');
    var toolSubType = $('#tool_sub_type');
    var toolPowerSource = $('#tool_power_source');
    var reservationSummary = document.getElementById('reservation_summary');
    var reservationDetails = $('#reservation_details');
    var rentedTools = $('#rented_tools');
    // var customSearch = $('#customSearch')
    var startDate = $('#startDate');
    var endDate = $('#endDate');
    var toolAvailabilityForm;
    var availableTools;
    var toolsInCart = [];
    var added = false;
    var checked = false;
    var calculated = false;
    var PowerSourceOption = [
        { value: 'all', text: 'All'},
        { value: 'electric', text: 'A/C Electric'},
        { value: 'cordless', text: 'D/C Cordless'},
        { value: 'gas', text: 'Gas'}
    ];
    var ManualOption = [{value:'manual',text:'Manual'}];
    var subTypeOption = {
        'all':[
            { value: 'all', text: 'All'}
        ],
        'Hand Tool':[
            { value: 'all', text: 'All'},
            { value: 'screwdriver', text: 'Screwdriver'},
            { value: 'socket', text: 'Socket'},
            { value: 'ratchet', text: 'Ratchet'},
            { value: 'wrench', text: 'Wrench'},
            { value: 'pliers', text: 'Pliers'},
            { value: 'gun', text: 'Gun'},
            { value: 'hammer', text: 'Hammer'}
        ],
        'Garden Tool':[
            { value: 'all', text: 'All'},
            { value: 'digger', text: 'Digger'},
            { value: 'pruner', text: 'Pruner'},
            { value: 'rakes', text: 'Rakes'},
            { value: 'wheelbarrows', text: 'Wheelbarrows'},
            { value: 'striking', text: 'Striking'}
        ],
        'Ladder Tool':[
            { value: 'all', text: 'All'},
            { value: 'straight', text: 'Straight'},
            { value: 'step', text: 'Step'}
        ],
        'Power Tool':[
            { value: 'all', text: 'All'},
            { value: 'drill', text: 'Drill'},
            { value: 'saw', text: 'Saw'},
            { value: 'sander', text: 'Sander'},
            { value: 'air-compressor', text: 'Air-Compressor'},
            { value: 'mixer', text: 'Mixer'},
            { value: 'generator', text: 'Generator'}
        ]
    };
    document.getElementById('startDate').valueAsDate = new Date();
    document.getElementById('endDate').valueAsDate = new Date();
    toolType.change(function(){
        var PowerOption = [{value:'all',text:'All'}];
        if(toolType.val() === 'Power Tool'){
            PowerOption = PowerSourceOption;
        } else if (toolType.val() !== 'all'){
            PowerOption = ManualOption;
        }
        toolPowerSource.empty();
        $.each(PowerOption, function(index,val){
            toolPowerSource.append($('<option>', {
                value: val.value,
                text: val.text
            }));
        });
        toolSubType.empty();
        $.each(subTypeOption[toolType.val()], function(index,val){
            toolSubType.append($('<option>', {
                value: val.value,
                text: val.text
            }));
        });
    });
    function descriptionConverter(description) {
        return '<a class="tool_detail" href="#" name="' + description + '">' + description + '</a>';
    }
    $('#toolAvailability-form').submit(function (e) {
        e.preventDefault();
        toolAvailabilityForm = {};
        toolAvailabilityForm.toolType = toolType.val();
        toolAvailabilityForm.powerSource = toolPowerSource.val();
        toolAvailabilityForm.subType = toolSubType.val();
        // toolAvailabilityForm.customSearch = "%" + customSearch.val() + "%";
        // toolAvailabilityForm.customSearch = "%driver%"
        toolAvailabilityForm.startDate = startDate.val();
        toolAvailabilityForm.endDate = endDate.val();
        if  (toolAvailabilityForm.startDate>=toolAvailabilityForm.endDate){
            alert("Start Date should be earlier than End Data. Reselect!");
            $('#content').empty();
        }

        else{
        $('#check-btn').prop('disabled', true);
        availableTools = [];
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/api/availabletools",
            data: JSON.stringify(toolAvailabilityForm),
            dataType: 'json',
            success: function(data) {
                $('#check-btn').prop('disabled', false);
                if (data.length > 0){
                    $.each(data, function(index, val) {
                        var toolId = val.toolId;
                        var description = val.shortDescription;
                        var depositPrice = val.depositPrice;
                        var rentalPrice = val.rentalPrice;
                        availableTools.push({
                            checkBox: "<input type='checkbox' value='"+ toolId + "' id='tool" + toolId + "''></input>",
                            toolId: toolId,
                            description: description,
                            depositPrice: depositPrice,
                            rentalPrice: rentalPrice
                        });
                    });
                    if(checked){
                        toolTable.dataTable().fnClearTable();
                        toolTable.dataTable().fnAddData(availableTools);
                        // if refresh the search results, empty the tool cart
                        if (toolsInCart.length > 0){
                            toolCart.dataTable().fnClearTable();
                            toolsInCart = [];
                        }
                    } else {
                        toolTable.dataTable({
                            // columnDefs: [ {
                            //     orderable: false,
                            //     className: 'select-checkbox',
                            //     targets:   0
                            // } ],
                            // select: {
                            //     style:    'os',
                            //     selector: 'td:first-child'
                            // },
                            // order: [[ 1, 'asc' ]],
                            processing: true,
                            data: availableTools,
                            sort: false,
                            searching: data.length > 10,
                            columns: [
                                {data: "checkBox"},
                                {data: "toolId"},
                                {data: "description",
                                    render: function(data, type, full) {
                                    return descriptionConverter(data);
                                }},
                                {data: "depositPrice"},
                                {data: "rentalPrice"}
                                // {data: "addTool"}
                            ]
                        });
                        checked = true;
                    }

                } else {
                    toolTable.dataTable().fnClearTable();
                }
            },
            error: function(e) {
                $('#check-btn').prop('disabled', false);
                alert("failed");
            }
        });
        }
    });

    $('#addToCart').click(function(){
        var newAvailable = [];
        $.each(availableTools, function(index, data){
            var checkbox = $('#tool'+data.toolId)[0];
            if (checkbox && checkbox.checked){
                toolsInCart.push(data);
            }else{
                newAvailable.push(data);
            }
        });
        availableTools = newAvailable;
        toolTable.dataTable().fnClearTable();
        if (availableTools.length > 0 ){
            toolTable.dataTable().fnAddData(availableTools);
        }
        if(added){
            toolCart.dataTable().fnClearTable();
            toolCart.dataTable().fnAddData(toolsInCart);
        } else {
            toolCart.dataTable({
                // columnDefs: [ {
                //     orderable: false,
                //     className: 'select-checkbox',
                //     targets:   0
                // } ],
                // select: {
                //     style:    'os',
                //     selector: 'td:first-child'
                // },
                // order: [[ 1, 'asc' ]],
                processing: true,
                data: toolsInCart,
                sort: false,
                searching: false,
                columns: [
                    {data: "checkBox"},
                    {data: "toolId"},
                    {data: "description",
                        render: function(data, type, full) {
                        return descriptionConverter(data);
                    }},
                    {data: "depositPrice"},
                    {data: "rentalPrice"}
                    // {data: "addTool"}
                ]
            });
            added = true;
        }
    });
    $('#removeFromCart').click(function(){
        var newToolsInCart = [];
        $.each(toolsInCart, function(index, data){
            var checkbox = $('#tool'+data.toolId)[0];
            if (checkbox.checked){
                availableTools.push(data);
            }else{
                newToolsInCart.push(data);
            }
        });
        toolsInCart = newToolsInCart;
        toolTable.dataTable().fnClearTable();
        toolTable.dataTable().fnAddData(availableTools);
        toolCart.dataTable().fnClearTable();
        if (toolsInCart.length > 0) {
            toolCart.dataTable().fnAddData(toolsInCart);
        }
    });

    function toolIDFormatter(toolId) {
        return '<span class="reservation-tool-id">' + toolId + '</span>';
    }

    $('#calculateTotal').click(function(){
        $('.control-buttons').removeClass('hide');
        $('#reservation_title').text("Reservation Summary");
        reservationDetails.empty();
        if (toolsInCart.length > 0){
            var detail = [];
            var rentedToolTable = [];
            var totalDeposit = 0.00;
            var totalRental = 0.00;
            var start = new Date(startDate.val());
            var end = new Date(endDate.val());
            var timeDiff = Math.abs(end.getTime() - start.getTime());
            var rentedDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
            for (var i = 0; i < toolsInCart.length; i++){
                totalDeposit += toolsInCart[i].depositPrice;
                totalRental += toolsInCart[i].rentalPrice;
            }
            totalDeposit = totalDeposit.toFixed(2);
            totalRental *= rentedDays;
            totalRental = totalRental.toFixed(2);
            // calculate rental price for each tool
            $.each(toolsInCart, function(index, data){
                rentedToolTable.push({
                    toolId: data.toolId,
                    description: data.description,
                    depositPrice: data.depositPrice,
                    rentalPrice: data.rentalPrice * rentedDays
                });
            });
            rentedToolTable.push({
                toolId: "<b>Total</b>",
                description: null,
                depositPrice: "<b>"+totalDeposit+"</b>",
                rentalPrice: "<b>"+totalRental+"</b>"
            });
            if(rentedDays > 0){
                detail.push("<li><p>Reservation Dates: "+startDate.val()+" - "+endDate.val()+"</p></li>");
                detail.push("<li><p>Number of Days Rented: "+rentedDays+"</p></li>");
                detail.push("<li><p>Total Deposit Price: "+totalDeposit+"</p></li>");
                detail.push("<li><p>Total Rental Price: "+totalRental+"</p></li>");
                if(calculated){
                    reservationDetails.empty();
                    rentedTools.dataTable().fnClearTable();
                    rentedTools.dataTable().fnAddData(rentedToolTable);
                }else{
                    rentedTools.dataTable({
                        processing: true,
                        data: rentedToolTable,
                        sort: false,
                        searching: false,
                        columns: [
                            {
                                data: "toolId",
                                render: function(data, type, full) {
                                    return toolIDFormatter(data);
                                }
                            },
                            {data: "description",
                                render: function(data, type, full) {
                                if (!data) {
                                    return "";
                                }
                                return descriptionConverter(data);
                            }},
                            {data: "depositPrice"},
                            {data: "rentalPrice"}
                        ]
                    });
                    calculated = true;
                }
                $( "<ul/>", {
                    "class": "my-new-list",
                    "id": "summary_detail",
                    html: detail.join( "" )
                }).appendTo(reservationDetails);
                reservationSummary.style.display = "block";
            }else{
                alert("Start/End date error!");
            }

        } else {
            alert("The tool cart is empty!");
        }
    });
    $('span').click(function(){
        reservationSummary.style.display = "none";
    });
    $('#reset_reservation').click(function() {
        toolTable.dataTable().fnAddData(toolsInCart);
        $.each(toolsInCart, function(index, data){
            availableTools.push(data);
        });
        toolsInCart = [];
        reservationSummary.style.display = "none";
        $('#toolCart').find('tbody').empty();
    });
    $('#submit_reservation').click(function(){
        var reservationConfirmForm = {};
        reservationConfirmForm.customerEmail = $('#email').val();
        reservationConfirmForm.startDate = startDate.val();
        reservationConfirmForm.endDate = endDate.val();
        var toolIdList = [];
        $('.reservation-tool-id').each(function() {
            var id = $(this).text().trim();
            if (/^[0-9]+$/g.test(id)) {
                toolIdList.push(id);
            }
        });
        reservationConfirmForm.toolIds = toolIdList;
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/api/confirmreservation",
            data: JSON.stringify(reservationConfirmForm),
            dataType: 'json',
            success: function (data) {
                $('#reservation_title').text("Reservation Confirmation");
                $("#summary_detail li:eq(0)").before("<li><p>Reservation Confirmation#: "+data+"</p></li>");
                $('.control-buttons').addClass("hide");
                $('#toolCart').find('tbody').empty();
            }, error: function(e) {
                alert("failed");
            }
        });
    });

    var toolDetail = $('#tool_detail_modal');
    var toolDetailId = $('#tool_detail_id');
    var toolDetailType = $('#tool_detail_type');
    var toolDetailShortDescription = $('#tool_detail_short_description');
    var toolDetailFullDescription = $('#tool_detail_full_description');
    var toolDetailSpecification = $('#tool_detail_specification');
    var toolDetailDepositPrice = $('#tool_detail_deposit_price');
    var toolDetailRentalPrice = $('#tool_detail_rental_price');
    var toolDetailAccessories = $('#tool_detail_accessories');

    $('body').on('click', '.tool_detail', function(e) {
        toolDetailAccessories.empty();
        toolDetailAccessories.parent().addClass('hide');
        var element = e.target;
        var toolId = $(element).parent().prev().text().trim();
        var link = '/api/tooldetail/' + toolId;

        $.ajax({
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            url: link,
            success: function(data) {
                toolDetailId.html(data.toolId);
                toolDetailType.html(data.toolType);
                toolDetailShortDescription.html(data.shortDescription);
                toolDetailFullDescription.html(data.fullDescription);
                toolDetailSpecification.html(data.specificDescription);
                toolDetailDepositPrice.html(data.depositPrice);
                toolDetailRentalPrice.html(data.rentalPrice);

                var accessories = data.accessories;
                if (accessories && accessories.length > 0) {
                    toolDetailAccessories.parent().removeClass('hide');
                    var accessories_arr = [];
                    $.each(accessories, function(i, v) {
                        accessories_arr.push("<li>" + v + "</li>");
                    })
                    toolDetailAccessories.html($("<ul/>").html(accessories_arr.join("")).html());
                }
                toolDetail.modal('show');
            }, error: function(e) {
                alert("failed");
            }
        })

    });
});
