package com.toolrental.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xinyu on 10/24/2017.
 */
@Controller
public class ClerkController {
    @RequestMapping(value = "/clerkhome", method = RequestMethod.GET)
    public String clerkHome(HttpServletRequest request, Model model) {
        return getLink(request, model, "clerkhome");
    }

    @RequestMapping(value = "/pickupreservation", method = RequestMethod.GET)
    public String pickUpReservation(HttpServletRequest request, Model model) {
        String link = getLink(request, model, "pickupreservation");
        String clerkEmail = (String) request.getSession().getAttribute("email");
        model.addAttribute("clerkEmail", clerkEmail);
        return link;
    }

    @RequestMapping(value = "/dropoffreservation", method = RequestMethod.GET)
    public String dropOffReservation(HttpServletRequest request, Model model) {
        String link = getLink(request, model, "dropoffreservation");
        String clerkEmail = (String) request.getSession().getAttribute("email");
        model.addAttribute("clerkEmail", clerkEmail);
        return link;
    }

    @RequestMapping(value = "/addnewtool", method = RequestMethod.GET)
    public String addNewTool(HttpServletRequest request, Model model) {
        return getLink(request, model, "addnewtool");
    }

    @RequestMapping(value = "/generatereports", method = RequestMethod.GET)
    public String generateReports(HttpServletRequest request, Model model) {
        return getLink(request, model, "generatereports");
    }

    @RequestMapping(value = "/clerkreport", method = RequestMethod.GET)
    public String clerkReport(HttpServletRequest request, Model model) {
        return getLink(request, model, "clerkreport");
    }

    @RequestMapping(value = "/customerreport", method = RequestMethod.GET)
    public String customerReport(HttpServletRequest request, Model model) {
        return getLink(request, model, "customerreport");
    }

    @RequestMapping(value = "/toolreport", method = RequestMethod.GET)
    public String toolReport(HttpServletRequest request, Model model) {
        return getLink(request, model, "toolreport");
    }


    private String getLink(HttpServletRequest request, Model model, String link) {
        String currentUser = (String) request.getSession().getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/";
        }
        model.addAttribute("user", currentUser);
        return link;
    }
}
