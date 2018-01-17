package com.toolrental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xinyu on 9/20/2017.
 */

@Controller
public class CustomerController {

    @RequestMapping(value = "/customerhome", method = RequestMethod.GET)
    public String customerHome(HttpServletRequest request, Model model) {
        return getPageLink(request, model, "customerhome");
    }

    @RequestMapping(value = "/viewprofile", method = RequestMethod.GET)
    public String viewProfile(HttpServletRequest request, Model model, @RequestParam(value="customerEmail", required = false) String customerEmail) {
        if (customerEmail != null) {
            model.addAttribute("email", customerEmail);
        } else {
            model.addAttribute("email", request.getSession().getAttribute("email"));
        }
        return getPageLink(request, model, "viewprofile");
    }

    @RequestMapping(value = "/checktoolavailability", method = RequestMethod.GET)
    public String checkToolAvailability(HttpServletRequest request, Model model) {
        return getPageLink(request, model,"checktoolavailability");
    }

    @RequestMapping(value = "/makereservation", method = RequestMethod.GET)
    public String makeReservation(HttpServletRequest request, Model model) {
        model.addAttribute("email", request.getSession().getAttribute("email"));
        return getPageLink(request, model, "makereservation");
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration() {
        return "registration";
    }

    private String getPageLink(HttpServletRequest request, Model model, String link) {
        String currentUser = (String) request.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/";
        } else {
            model.addAttribute("user", currentUser);
            return link;
        }
    }
}
