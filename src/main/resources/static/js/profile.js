$(document).ready(function() {
    var content = $('#user_profile');
    var email = $('#email').val();
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
    // the user email was hard coded as "user_1@gmail.com"
    var link = "/api/viewprofile?userEmail="+email;
    $.getJSON(link, function(data) {
        var userProfile = [];
        var userEmail = data.userDetail.userEmail;
        var fullName = data.userDetail.fullName;
        var address = data.userDetail.fullAddress;

        userProfile.push( "<li>E-mail" + ": " + userEmail + "</li>" );
        userProfile.push( "<li>Full Name" + ": " + fullName + "</li>" );
        $.each(data.phoneNumberList, function(key, val) {
            var phoneType = getPhoneType(val.subType);
            var phoneNumber = val.phoneNumber;
            userProfile.push( "<li>" + phoneType + ": " + phoneNumber + "</li>" );
        });
        userProfile.push( "<li>Address" + ": " + address + "</li>" );
        $( "<ul/>", {
            "class": "my-new-list",
            html: userProfile.join( "" )
        }).appendTo(content);

        var reservationTable = $('#user_reservation');
        reservationTable.dataTable({
            processing: true,
            data: data.userReservation,
            sort: false,
            searching: false,
            columns: [
                // {title : "Reservation ID"},
                // {title : "Tools"},
                // {title : "Start Date"},
                // {title : "End Date"},
                // {title : "PickUp Clerk"},
                // {title : "DropOff Clerk"},
                // {title : "No. Of Days"},
                // {title : "Total Deposit Price"},
                // {title : "Total Rental Price"},
                {data: "reservationId"},
                {data: "tools"},
                {data: "startDate"},
                {data: "endDate"},
                {data: "pickUpClerk"},
                {data: "dropOffClerk"},
                {data: "numberOfDays"},
                {data: "totalDepositPrice"},
                {data: "totalRentalPrice"}
            ]
        });
    });
});
