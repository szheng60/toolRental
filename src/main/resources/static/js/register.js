$(document).ready(function() {

    $('.phone-number').change(function(e) {
        var currentInput = e.target;
        var result = currentInput.checkValidity();
        var parent = $(currentInput).closest('.form-group');
        if (result === false) {
            parent.addClass('has-danger error');
            parent.find('.error-msg').removeClass('hide');
        } else {
            parent.removeClass('has-danger error');
            parent.find('.error-msg').addClass('hide');
        }
    });

    $('#re_type_password').change(function(e) {
        var currentInput = e.target;
        var parent = $(currentInput).closest('.form-group');
        if ($(currentInput).val() !== $('#password').val()) {
            confirm("password not matching");
            parent.addClass('has-danger error');
        } else {
            parent.removeClass('has-danger error');
        }
    })

    function reformatPhoneNumber(phone) {
        var phoneNumber = {};
        phoneNumber.areaCode = "";
        phoneNumber.phoneNumber = "";
        phoneNumber.extension = "";

        if (phone.length < 10) {
            return phoneNumber;
        } else {
            if (phone.length == 10) {
                phoneNumber.areaCode = phone.substring(0, 3);
                phoneNumber.phoneNumber = phone.substring(3, 10);
            } else {
                var number = phone.split('x')[0];
                var extension = phone.split('x')[1];
                phoneNumber = reformatPhoneNumber(number);
                phoneNumber.extension = extension;
            }
        }
        return phoneNumber;
    }
    $(':input').change(function() {
        $(this).closest('.form-group').removeClass('has-danger error');
    })

    $('#registration_form').submit(function(e) {
        e.preventDefault();
        if ($('.error').length > 0) {
            return;
        }
        var firstName = $('#first_name').val();
        var midName = $('#middle_name').val();
        var lastName = $('#last_name').val();
        var email = $('#email_address').val();
        var password = $('#password').val();
        var retypePassword = $('#re_type_password').val();
        var homePhone = reformatPhoneNumber($('#home_phone').val());
        var workPhone = reformatPhoneNumber($('#work_phone').val());
        var cellPhone = reformatPhoneNumber($('#cell_phone').val());
        var street = $('#street_address').val();
        var city = $('#city').val();
        var state = $('#state').val();
        var zipcode = $('#zip').val();

        var nameOnCreditCard = $('#name_on_credit_card').val();
        var cardNumber = $('#credit_card_number').val();
        var cardExpirationMonth = $('#expiration_month').val();
        var cardExpirationYear = $('#expiration_year').val();
        var cardCvc = $('#cvc').val();
        var primaryPhone = $('input[name=phone-type]:checked').val();

        var registrationForm = {};
        registrationForm.user = {
            firstName: firstName,
            midName: midName,
            lastName: lastName,
            email: email,
            password: password
        };
        registrationForm.phoneNumber = [{
            customerEmail: email,
            subType: "c",
            areaCode: cellPhone.areaCode,
            phoneNumber: cellPhone.phoneNumber,
            extension: cellPhone.extension
        }, {
            customerEmail: email,
            subType: "h",
            areaCode: homePhone.areaCode,
            phoneNumber: homePhone.phoneNumber,
            extension: homePhone.extension
        }, {
            customerEmail: email,
            subType: "w",
            areaCode: workPhone.areaCode,
            phoneNumber: workPhone.phoneNumber,
            extension: workPhone.extension
        }],
        registrationForm.creditCard = {
            number: cardNumber,
            nameOnCard: nameOnCreditCard,
            cvc: cardCvc,
            expMonth: cardExpirationMonth,
            expYear: cardExpirationYear
        };
        registrationForm.customer = {
            userEmail: email,
            primaryPhone: primaryPhone,
            street: street,
            city: city,
            state: state,
            zipCode: zipcode,
            cardNo: cardNumber
        };
        $('#register-btn').prop('disabled', true);

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/api/register",
            data: JSON.stringify(registrationForm),
            dataType: 'json',
            success: function(data) {
                $('#register-btn').prop('disabled', false);
                alert(data.msg);
                var next = document.getElementById("nextUrl");
                next.href = "/";
                next.click();
            },
            error: function(request, status, error) {
                $('#register-btn').prop('disabled', false);
                alert("Failed to register. Check email or credit card.");
            }
        });
    });
})