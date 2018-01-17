/**
 * Created by xinyu on 11/4/2017.
 */
$(document).ready(function() {
    var customerTable = $('#customer_table');
    var profileTable = $('#profile_table');
    var content = $('#user_profile');
    var reservation = $('#user_reservation');
    var modal = $('#profileModal');
    function buildViewProfileLink(email) {
        return '<a class="viewProfile" href="#" name="' + email + '">' + "View Profile" + '</a>';
    }

    function getPhoneType(type) {
        switch(type) {
            case 'c':
                return 'Cell';
            case 'h':
                return 'Home';
            case 'w':
                return 'Work';
        }
    }

    $('#customer_table').on('click', '.viewProfile', function(e) {
        $('#user_profile').empty();
        var element = e.target;
        var email = element.name;

        var link = "/api/viewprofile?userEmail=" + email;

        $.getJSON(link, function(data) {
            var userProfile = [];
            var userEmail = data.userDetail.userEmail;
            var fullName = data.userDetail.fullName;
            var address = data.userDetail.fullAddress;

            userProfile.push( "<li><strong>E-mail" + ":</strong> " + userEmail + "</li>" );
            userProfile.push( "<li><strong>Full Name" + ":</strong> " + fullName + "</li>" );
            $.each(data.phoneNumberList, function(key, val) {
                var phoneType = getPhoneType(val.subType);
                var phoneNumber = val.phoneNumber;
                userProfile.push( "<li><strong>" + phoneType + ":</strong> " + phoneNumber + "</li>" );
            });
            userProfile.push( "<li><strong>Address" + ":</strong> " + address + "</li>" );
            $( "<ul/>", {
                "class": "my-new-list",
                html: userProfile.join( "" )
            }).appendTo(content);


            profileTable.dataTable({
                destroy: true,
                data: data.userReservation,
                searching: false,
                columns: [
                    {data : "reservationId"},
                    {data : "tools"},
                    {data : "startDate"},
                    {data : "endDate"},
                    {data : "pickUpClerk"},
                    {data : "dropOffClerk"},
                    {data : "numberOfDays"},
                    {data : "totalDepositPrice"},
                    {data : "totalRentalPrice"}
                ]
            });

        });
        modal.modal('show');

    })

    $.getJSON("/api/customerreport", function(response) {
        customerTable.dataTable({
            sort: false,
            data: response,
            searching: false,
            columns: [
                {data : "customerId"},
                {
                    data : "profileEmail",
                    render: function(data, type, full) {
                        return buildViewProfileLink(data);
                    }
                },
                {data : "firstName"},
                {data : "midName"},
                {data : "lastName"},
                {data : "email"},
                {data : "phone"},
                {data : "totalReservations"},
                {data : "totalToolsRented"}
            ]
        });
    });
})
