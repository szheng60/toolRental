/**
 * Created by xinyu on 11/5/2017.
 */
$(document).ready(function() {
    var currentCustomerId;

    var reservationTable = $('#reservation_table');
    var reservationDetail = $('#reservation_detail');
    var mainReservationDetail = $('#main_reservation_detail');
    var clerkEmail = $('#clerk_email').val();
    // var rentalContract = $('#rental_contract');
    var contractInfo = $('#rental_contract');
    var reservationToolTable = $('#reservation_tool_table');
    var dropoffToolTable = $('#dropoff_tool_table');
    // tool info

    var toolDetailId = $('#tool_detail_id');
    var toolDetailType = $('#tool_detail_type');
    var toolDetailShortDescription = $('#tool_detail_short_description');
    var toolDetailFullDescription = $('#tool_detail_full_description');
    var toolDetailSpecification = $('#tool_detail_specification');
    var toolDetailDepositPrice = $('#tool_detail_deposit_price');
    var toolDetailRentalPrice = $('#tool_detail_rental_price');
    var toolDetailAccessories = $('#tool_detail_accessories');
    // modal
    var pickupConfirm= document.getElementById('pickupconfirm-form');
    var rentalContract= document.getElementById('rentalcontract');
    var toolDetail = $('#tool_detail_modal');
    var dropoffdetail= document.getElementById('dropoffdetail-form');

    $('input[name=eorn]').change(function() {
        if ($(this).val() === "w") {
            $('#update_credit_card_section').removeClass('hide');
            $('#update_creditcard_btn').removeClass('hide');
        } else {
            $('#update_credit_card_section').addClass('hide');
            $('#update_creditcard_btn').addClass('hide');
        }
    });


    $('#pickup-btn').click(function(){

        var reservationId = $('#reservation_id').val();
        if (reservationId){
            $('.reservation_id').each(function() {
                if ($(this).attr('name') === reservationId) {
                    currentCustomerId = $(this).closest('tr').find('.customer-id').text().trim();
                    return false;
                }
            });

            var link = "/api/reservation/" + reservationId;
            $.getJSON(link, function(response) {
                reservationDetail.empty();
                var detail = [];
                detail.push("<li>Pickup Clerk: " + clerkEmail + "</li>");
                detail.push( "<li>Reservation ID: " + response.reservationId + "</li>" );
                detail.push("<li>Customer Name: " + response.customerFullName + "</li>");
                detail.push("<li>Total Deposit: " + response.totalDeposit + "</li>");
                detail.push("<li>Total Rental Price: " + response.totalRentalPrice + "</li>");

                $( "<ul/>", {
                    html: detail.join( "" )
                }).appendTo(reservationDetail);
            });
            pickupConfirm.style.display = "block";
        } else {
            alert("The reservation ID is empty!");
        }
    });

    function updateCreditCard() {
        var nameOnCreditCard = $('#name_on_credit_card').val();
        var cardNumber = $('#credit_card_number').val();
        var cardExpirationMonth = $('#expiration_month').val();
        var cardExpirationYear = $('#expiration_year').val();
        var cardCvc = $('#cvc').val();
        var creditCard = {
            number: cardNumber,
            nameOnCard: nameOnCreditCard,
            cvc: cardCvc,
            expMonth: cardExpirationMonth,
            expYear: cardExpirationYear
        }
        if (currentCustomerId) {
            $.ajax({
                type: "POST",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(creditCard),
                url: "/api/updatecreditcard?customerId=" + currentCustomerId,
                success: function (data) {
                    alert("credit card updated");
                }, error: function (e) {
                    alert("failed to update credit card")
                }
            });
        }
    }

    $('#update_creditcard_btn').click(function() {
        updateCreditCard();
    });
    var pickupContract;
    $('#confirmpickup-btn').click(function(){
        pickupConfirm.style.display = "none";
        var reservationId = $('#reservation_id').val();
        var contractLink = "api/rentalcontract?reservationId=" + reservationId + "&clerkEmail=" + clerkEmail;

        $.getJSON(contractLink, function(response) {
            pickupContract = response;
            contractInfo.empty();
            var contract = [];
            contract.push("<li>Pick-up Clerk: " + response.pickUpClerkName + "</li>");
            contract.push("<li>Customer Name: " + response.customerName + "</li>");
            contract.push("<li>Credit Card #: " + response.creditCardNumber + "</li>");
            contract.push("<li>Start Date: " + response.startDate + "</li>");
            contract.push("<li>End Date: " + response.endDate + "</li>");
            $( "<ul/>", {
                html: contract.join( "" )
            }).appendTo(contractInfo);

            reservationToolTable.dataTable({
                destroy: true,
                data: response.reservedTools,
                searching: false,
                columns: [
                    {
                        data : "toolId"
                    },
                    {
                        data : "shortDescription",
                        render: function(data, type, full) {
                            return descriptionConverter(data);
                        }
                    },
                    // here the price is daily price
                    { data : "depositPrice"},
                    { data : "rentalPrice"}
                ]
            });
            confirmPickup(pickupContract);
        });


        rentalContract.style.display = "block";
    });

    function confirmPickup(pickupContract) {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(pickupContract),
            url: "/api/confirmpickup?clerkEmail="+clerkEmail,
            success: function(data) {
                alert("pick up confirmed successfully");
            }, error: function(e) {
                alert("confirm pick up failed");
            }
        });
    }

    $('#print-btn').click(function(){
        alert("The contract is printed!");
    });
    var dropoffContract;
    $('#dropoff-btn').click(function(){
        var reservationId = $('#reservation_id').val();
        var contractLink = "api/rentalcontract?reservationId=" + reservationId + "&clerkEmail=" + clerkEmail;
        if (reservationId){
            var link = "/api/reservation/" + reservationId;
            $.getJSON(link, function(response) {
                reservationDetail.empty();
                var detail = [];
                detail.push( "<li>Reservation ID: " + response.reservationId + "</li>" );
                detail.push("<li>Customer Name: " + response.customerFullName + "</li>");
                detail.push("<li>Total Deposit: " + response.totalDeposit + "</li>");
                detail.push("<li>Total Rental Price: " + response.totalRentalPrice + "</li>");
                detail.push("<li>Total Due: " + (response.totalDeposit - response.totalRentalPrice).toFixed(2) + "</li>");
                $( "<ul/>", {
                    html: detail.join( "" )
                }).appendTo(reservationDetail);
            });

            $.getJSON(contractLink, function(response) {
                // NEED fill in the tool table
                dropoffContract = response;
                dropoffToolTable.dataTable({
                    destroy: true,
                    data: response.reservedTools,
                    searching: false,
                    columns: [
                        {
                            data : "toolId"
                        },
                        {
                            data : "shortDescription",
                            render: function(data, type, full) {
                                return descriptionConverter(data);
                            }
                        },
                        // here the price is daily price
                        { data : "depositPrice"},
                        { data : "rentalPrice"}
                    ]
                });
            });
            dropoffdetail.style.display = "block";
        } else {
            alert("The reservation ID is empty!");
        }
    });
    $('#confirmdropoff-btn').click(function(){
        $.ajax({
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(dropoffContract),
            url: "/api/confirmdropoff?clerkEmail="+clerkEmail,
            success: function(data) {
                alert("drop off confirmed successfully");
                dropoffdetail.style.display = "none";
            }, error: function(e) {
                alert("confirm drop off failed");
            }
        });
    });
    $('#close_pickup_form').click(function(){
        pickupConfirm.style.display = "none";
    });
    $('#close_rental_contract').click(function(){
        rentalContract.style.display = "none";
    });
    $('#close_drop_off_form').click(function(){
        dropoffdetail.style.display = "none";
    });


    //

    function reservationIdConverter(reservationId) {
        return '<a class="reservation_id" href="#" name="' + reservationId + '">' + reservationId + '</a>';
    }

    function customerIdConverter(customerId) {
        return '<span class="customer-id">' + customerId + '</span>';
    }

    function descriptionConverter(description) {
        return '<a class="tool_detail" href="#" name="' + description + '">' + description + '</a>';
    }

    var loadLink;
    if ($('#page_title').text().trim() == "Pickup Reservation") {
        loadLink = "/api/reservationlist/pickup";
    } else {
        loadLink = "/api/reservationlist/dropoff";
    }
    $.getJSON(loadLink, function(response) {
        reservationTable.dataTable({
            destroy: true,
            data: response,
            searching: false,
            columns: [
                {
                    data : "reservationId",
                    render: function(data, type, full) {
                        return reservationIdConverter(data);
                    }
                },
                { data : "customerShortName"},
                { data : "customerId",
                    render: function(data, type, full) {
                    return customerIdConverter(data);
                    }
                },
                { data : "startDate"},
                { data : "endDate"}
            ]
        })
    });

    reservationTable.on('mouseenter', '.reservation_id', function(e) {
        var element = e.target;
        var reservationId = $(element).attr('name');
        var link = "/api/reservation/" + reservationId;

        $.getJSON(link, function(response) {
            mainReservationDetail.empty();
            var detail = [];
            detail.push( "<li>Reservation ID: " + response.reservationId + "</li>" );
            detail.push("<li>Customer Name: " + response.customerFullName + "</li>");
            detail.push("<li>Total Deposit: " + response.totalDeposit + "</li>");
            detail.push("<li>Total Rental Price: " + response.totalRentalPrice + "</li>");

            $( "<ul/>", {
                html: detail.join( "" )
            }).appendTo(mainReservationDetail);
        });
    });

    reservationTable.on('mouseleave', '.reservation_id', function(e) {
        mainReservationDetail.empty();
    });

    $('body').on('click', '.tool_detail', function(e) {
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
                console.log(accessories);
                if (accessories && accessories.length > 0) {
                    toolDetailAccessories.parent().removeClass('hide');
                    var accessories_arr = [];
                    $.each(accessories, function(i, v) {
                        accessories_arr.push("<li>" + v + "</li>");
                    });
                    toolDetailAccessories.html($("<ul/>").html(accessories_arr.join("")).html());
                }
                toolDetail.modal('show');
            }, error: function(e) {
                alert("failed");
            }
        })

    });


});