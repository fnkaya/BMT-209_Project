package com.stoptakip.dao.daoabstract;

import com.stoptakip.dto.complexmodels.ComplexAccount;
import com.stoptakip.dto.models.Account;

import java.util.ArrayList;

public interface IAccountServices extends IGenericServices<Account> {

    ArrayList<ComplexAccount> getAllComplex();
}
