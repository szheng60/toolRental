$(document).ready(function() {
    // var dateControl = document.querySelector('input[type="Date"]');
    // dateControl.value = '2017-06-01';
    var toolTable = $('#content');
    var toolDetail = $('#toolDetail');
    var toolType = $('#tool_type');
    var toolSubType = $('#tool_sub_type');
    var toolPowerSource = $('#tool_power_source');
    // var customSearch = $('#customSearch')
    var startDate = $('#startDate');
    var endDate = $('#endDate');
    var checked = false;
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
    $('#toolAvailability-form').submit(function (e) {
        e.preventDefault();
        var toolAvailabilityForm = {};
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

            var availableTools = [];
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
                                toolId: "<a href='#' class='tool_id'>" + toolId + "</a>",
                                description: description,
                                depositPrice: depositPrice,
                                rentalPrice: rentalPrice
                            });
                        });
                        if(checked){
                            toolTable.dataTable().fnClearTable();
                            toolTable.dataTable().fnAddData(availableTools);
                        } else {
                            toolTable.dataTable({
                                processing: true,
                                data: availableTools,
                                sort: false,
                                searching: data.length > 10,
                                columns: [
                                    {data: "toolId"},
                                    {data: "description"},
                                    {data: "depositPrice"},
                                    {data: "rentalPrice"}
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
    var clicked = false;
    $(toolTable).click('.toolId', function(e) {
        e.preventDefault();
        var detail = [];
        var toolID = $(e.target).text().trim();
        var URL = "/api/tooldetail/" + toolID;
        $.ajax({
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            url: URL,
            success: function(data) {
                detail.push("<h3>Tool Details:</h3>");
                detail.push("<h5>Tool ID</h5>");
                detail.push("<li>" + data.toolId + "</li>");
                detail.push("<h5>Short Description</h5>");
                detail.push("<li>" + data.shortDescription + "</li>");
                detail.push("<h5>Full Description</h5>");
                detail.push("<li>" + data.fullDescription + "</li>");
                detail.push("<li>" + data.specificDescription + "</li>");
                detail.push("<h5>Deposit Price</h5>");
                detail.push("<li>" + data.depositPrice + "</li>");
                detail.push("<h5>Rental Price</h5>");
                detail.push("<li>" + data.rentalPrice + "</li>");
                $.each(data.accessories, function(i, v) {
                    detail.push("<li>" + v + "</li>");
                })
                if (clicked) {
                    toolDetail.empty();
                } else {
                    clicked = true;
                }
                $( "<ul/>", {
                    "class": "my-new-list",
                    html: detail.join( "" )
                }).appendTo(toolDetail);
            },
            error: function(e) {
                // alert("failed");
            }
        });
    });
});
