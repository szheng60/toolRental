/**
 * Created by xinyu on 11/4/2017.
 */
$(document).ready(function() {
    var clerkTable = $('#clerkTable');
    $.getJSON('/api/clerkreport', function(response) {
        clerkTable.dataTable({
            processing: true,
            data: response,
            sort: false,
            searching: false,
            columns: [
                {data : "clerkId"},
                {data : "firstName"},
                {data : "midName"},
                {data : "lastName"},
                {data : "email"},
                {data : "hireDate"},
                {data : "numberOfPickups"},
                {data : "numberOfDropoffs"},
                {data : "combinedTotal"}
            ]

        });

    });
})