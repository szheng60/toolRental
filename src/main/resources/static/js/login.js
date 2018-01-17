$(document).ready(function() {
    $('#login-form').submit(function(e) {
        e.preventDefault();

        var loginForm = {};
        loginForm["email"] = $("#inputEmail").val().trim();
        loginForm["password"] = $("#inputPassword").val().trim();
        var role = $("input[name=roleRadios]:checked").val().trim();
        loginForm["role"] = role;
        var next = document.getElementById("nextUrl");
        $('#login-btn').prop('disabled', true);

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/api/login",
            data: JSON.stringify(loginForm),
            dataType: 'json',
            success: function(data) {

                    next.href = data.msg;
                    next.click();

            },
            error: function (e) {
                var json = jQuery.parseJSON(e.responseText);
                if (role == "1" && json["status"] == 2) {
                    next.href="/registration";
                    next.click();
                } else if (json["status"] == 99) {
                    $('#login-form').addClass('hide');
                    $('#new_password_for_first_time').removeClass('hide');
                }
                else{
                    $('#feedback').html(json["msg"]);
                    $('#login-btn').prop('disabled', false);
                }
            }
        })
    })

    $('#new_input_password_btn').click(function() {
        $('#feedback').empty();
        updatePasswordForFirstTime();
    })

    function updatePasswordForFirstTime() {
        var email = $("#inputEmail").val().trim();
        var input = $('#new_input_password').val();
        var confirmInput = $('#new_input_password_confirm').val();

        if (input != confirmInput) {
            alert("password not matched");
        } else {
            var next = document.getElementById("nextUrl");
            var updateClerk = {};
            updateClerk.email = email;
            updateClerk.password = input;
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "api/login/update",
                dataType: 'json',
                data: JSON.stringify(updateClerk),
                success: function(data) {
                    next.href = "/";
                    next.click();
                }, error: function(e) {
                    alert("failed to update password");
                }
            })
        }
    }
})

