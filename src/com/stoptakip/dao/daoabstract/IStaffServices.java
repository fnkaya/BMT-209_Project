package com.stoptakip.dao.daoabstract;

import com.stoptakip.dto.models.Staff;

import java.util.ArrayList;

public interface IStaffServices extends IExtendedGenericServices<Staff> {

    ArrayList<Staff> getAllActive();
    Staff getByEMail();
}
