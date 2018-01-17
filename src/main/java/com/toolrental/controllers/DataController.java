package com.toolrental.controllers;

import com.toolrental.form.*;
import com.toolrental.model.*;
import com.toolrental.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by xinyu on 10/17/2017.
 */

@RestController
public class DataController {

    @Autowired
    private ILoginService loginService;

    @Autowired
    private IRegistrationService registrationService;

    @Autowired
    private IProfileService profileService;

    @Autowired
    private IAvailableToolService availableToolService;

    @Autowired
    private IToolDetailService toolDetailService;

    @Autowired
    private IMakeReservationService makeReservationService;

    @Autowired
    private IClerkReportService clerkReportService;

    @Autowired
    private ICustomerReportService customerReportService;

    @Autowired
    private IToolReportService toolReportService;

    @Autowired
    private IPickUpReservationService pickUpReservationService;

    @Autowired
    private IDropOffReservationService dropOffReservationService;

    @Autowired
    private IAddToolService addToolService;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(HttpServletRequest request, @Valid @RequestBody LoginForm loginForm, Errors errors) {
        LoginResult result = loginService.login(loginForm);
        if (result.getStatus() != 4) {
            return ResponseEntity.badRequest().body(result);
        }
        request.getSession().setAttribute("currentUser", result.getLoginName());
        request.getSession().setAttribute("email", loginForm.getEmail());

        return ResponseEntity.ok(result);
    }

    @PostMapping("api/login/update")
    public ResponseEntity<?> loginUpdate(HttpServletRequest request, @RequestBody User user, Errors errors) {
        LoginResult result = loginService.loginUpdate(user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> register(HttpServletRequest request, @RequestBody RegistrationForm registrationForm, Errors errors) {

        RegistrationResult result = null;
        try {
            result = registrationService.registerNewCustomer(registrationForm);
        } catch(Exception e) {
            System.out.println("BAD");
        }
        if (result == null) {
            return ResponseEntity.badRequest().body(result);
        }
        //request.getSession().setAttribute("currentUser", registrationForm.getCustomer().getUserEmail());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/viewprofile")
    public ResponseEntity<UserProfile>viewProfile(HttpServletRequest request, @RequestParam("userEmail") String userEmail) {
        String emailForViewProfile = null;
        if (userEmail != null) {
            emailForViewProfile = userEmail;
        } else {
            emailForViewProfile = (String) request.getSession().getAttribute("currentUser");
        }
        UserProfile userProfile = new UserProfile();

        userProfile = profileService.retrieveUserProfile(emailForViewProfile);

        return ResponseEntity.ok(userProfile);
    }
    @PostMapping("/api/availabletools")
    public ResponseEntity<?> availableTools(HttpServletRequest request, @RequestBody ToolAvailabilityForm toolAvailabilityForm, Errors errors) {
        List<ToolDetail> toolDetailList = availableToolService.availableToolList(toolAvailabilityForm);

        return ResponseEntity.ok(toolDetailList);
    }

    @PostMapping("/api/tooldetail/{id}")
    public ResponseEntity<?> toolDetail(HttpServletRequest request, @PathVariable("id") int id) {
        ToolDetail td = toolDetailService.retrieveToolDetail(id);
        return ResponseEntity.ok(td);
    }

    @PostMapping("/api/upcomingtoolsreturn")
    public ResponseEntity<?> upcomingToolsReturn(HttpServletRequest request, @RequestBody ToolAvailabilityForm toolAvailabilityForm, Errors errors) {
        List<Integer> toolsReturnList = makeReservationService.getListOfUpcomingReturnToolIds(toolAvailabilityForm);

        return ResponseEntity.ok(toolsReturnList);
    }

    @PostMapping("/api/confirmreservation")
    public ResponseEntity<?> confirmReservation(HttpServletRequest request, @RequestBody MakeReservationForm makeReservationForm, Errors errors) {
        int confirmationNumber = makeReservationService.confirmReservation(makeReservationForm);
        return ResponseEntity.ok(confirmationNumber);
    }

    @GetMapping("/api/clerkreport")
    public ResponseEntity<?> generateClerkReport(HttpServletRequest request) {
        Calendar cal = Calendar.getInstance();
        int pastMonth = cal.get(Calendar.MONTH);

        List<ClerkReport> clerkReportList = clerkReportService.retrieveAllClerkReport(pastMonth);

        return ResponseEntity.ok(clerkReportList);
    }

    @GetMapping("/api/customerreport")
    public ResponseEntity<?> generateCustomerReport(HttpServletRequest request) {
        Calendar cal = Calendar.getInstance();
        int pastMonth = cal.get(Calendar.MONTH);

        List<CustomerReport> clerkReportList = customerReportService.retrieveAllCustomerReport(pastMonth);

        return ResponseEntity.ok(clerkReportList);
    }

    @GetMapping("/api/toolreport/{filter}")
    public ResponseEntity<?> generateToolReport(@PathVariable("filter") String filter) {
        List<ToolReport> toolReportList = toolReportService.retrieveToolReport(filter);
        return ResponseEntity.ok(toolReportList);
    }

    @GetMapping("/api/reservationlist/{type}")
    public ResponseEntity<?> getReservationList(@PathVariable("type") String type) {
        List<Reservation> reservationList = new ArrayList<>();
        if ("dropoff".equalsIgnoreCase(type)) {
            reservationList = dropOffReservationService.retrieveReservationList();
        } else if ("pickup".equalsIgnoreCase(type)) {
            reservationList = pickUpReservationService.retrieveReservationList();
        }
        return ResponseEntity.ok(reservationList);
    }

    @GetMapping("/api/reservation/{id}")
    public ResponseEntity<?> getReservationiDetail(@PathVariable("id") int reservationId) {
        ReservationDetail rd = pickUpReservationService.retrieveReservationDetail(reservationId);
        return ResponseEntity.ok(rd);
    }
    @PostMapping("/api/updatecreditcard")
    public ResponseEntity<?> updateCreditCard(HttpServletRequest request, @RequestBody CreditCard creditCard, Errors errors,
                                              @RequestParam("customerId") String customerId) {
        boolean result = pickUpReservationService.updateCreditCard(creditCard, customerId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/rentalcontract")
    public ResponseEntity<?> getRentalContract(@RequestParam("reservationId") int reservationId, @RequestParam("clerkEmail") String clerkEmail) {
        RentalContract rc = pickUpReservationService.retrieveRentalContract(reservationId, clerkEmail);
        return ResponseEntity.ok(rc);
    }

    @PostMapping("/api/confirmpickup")
    public ResponseEntity<?> confirmPickUp(@RequestBody RentalContract rc, HttpServletRequest request,  Errors errors,
                                           @RequestParam("clerkEmail") String clerkEmail) {
        boolean confirmed = pickUpReservationService.rentalContractConfirmed(rc, clerkEmail);
        return ResponseEntity.ok(confirmed);
    }
    @PostMapping("/api/confirmdropoff")
    public ResponseEntity<?> confirmDropOff(@RequestBody RentalContract rc, HttpServletRequest request,  Errors errors,
                                           @RequestParam("clerkEmail") String clerkEmail) {
        boolean confirmed = dropOffReservationService.rentalContractConfirmed(rc, clerkEmail);
        return ResponseEntity.ok(confirmed);
    }

    @PostMapping("/api/addtool")
    public ResponseEntity<?> addTool(HttpServletRequest request, @RequestBody AddToolForm addToolForm, Errors errors) {
        boolean confirmed = addToolService.addTool(addToolForm);
        return ResponseEntity.ok(confirmed);
    }
}
