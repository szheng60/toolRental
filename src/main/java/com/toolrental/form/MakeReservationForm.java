package com.toolrental.form;

import java.util.List;

/**
 * Created by xinyu on 11/28/2017.
 */
public class MakeReservationForm {
    private String customerEmail;
    private String startDate;
    private String endDate;
    private List<Integer> toolIds;

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Integer> getToolIds() {
        return toolIds;
    }

    public void setToolIds(List<Integer> toolIds) {
        this.toolIds = toolIds;
    }
}
