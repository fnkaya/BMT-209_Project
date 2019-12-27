package com.stoptakip.dto.complexmodels;

import com.stoptakip.dto.models.Account;

public class ComplexAccount extends Account {

    private String staff_name;
    private String staff_e_mail;

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public void setStaff_e_mail(String staff_e_mail) {
        this.staff_e_mail = staff_e_mail;
    }

    @Override
    public String toString() {
        return staff_e_mail +" ("+ staff_name +")";
    }
}
