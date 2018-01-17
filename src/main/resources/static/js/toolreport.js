/**
 * Created by xinyu on 11/4/2017.
 */
$(document).ready(function() {
    var toolTable = $('#toolTable');
    var toolDetail = $('#tool_detail_modal');
    var toolDetailId = $('#tool_detail_id');
    var toolDetailType = $('#tool_detail_type');
    var toolDetailShortDescription = $('#tool_detail_short_description');
    var toolDetailFullDescription = $('#tool_detail_full_description');
    var toolDetailSpecification = $('#tool_detail_specification');
    var toolDetailDepositPrice = $('#tool_detail_deposit_price');
    var toolDetailRentalPrice = $('#tool_detail_rental_price');
    var toolDetailAccessories = $('#tool_detail_accessories');

    function getStatusColorCode(status) {
        var colorCode = 'green';
        status = status.toLowerCase();
        switch (status) {
            case 'for-sale':
                colorCode = 'darkgray';
                break;
            case 'rented':
                colorCode = 'yellow';
                break;
            case 'sold':
                colorCode = 'black';
                break;
            case 'in-repair':
                colorCode = 'red';
                break;
        }
        return colorCode;
    }

    $('input[name=tool_type]').click(function(e) {
        var element = e.target;
        var filterCriteria = $(element).val();
        buildToolTable(filterCriteria);
    });

    toolTable.on('click', '.tool_detail', function(e) {
        toolDetailAccessories.empty();
        toolDetailAccessories.parent().addClass('hide');
        var element = e.target;
        var toolId = $(element).parent().parent().find('.sorting_1').text().trim();
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

    function statusFormatter(status) {
        return '<span class="tool_status" style="background-color:' + getStatusColorCode(status) + ';">' + status + '</span>';
    }

    function descriptionConverter(description) {
        return '<a class="tool_detail" href="#" name="' + description + '">' + description + '</a>';
    }

    function buildToolTable(filterCriteria) {
        var link = '/api/toolreport/' + filterCriteria;
        $.getJSON(link, function(response) {
            toolTable.dataTable({
                destroy: true,
                data: response,
                columns: [
                    {data : "toolId"},
                    {
                        data : "currentStatus",
                        render : function(data, type, full) {
                            return statusFormatter(data);
                        }
                    },
                    {data : "date"},
                    {
                        data : "description",
                        render: function(data, type, full) {
                            return descriptionConverter(data);
                        }
                    },
                    {data : "rentalProfit"},
                    {data : "totalCost"},
                    {data : "totalProfit"},
                ]

            });

        });
    }

    buildToolTable('All_Tool');
})