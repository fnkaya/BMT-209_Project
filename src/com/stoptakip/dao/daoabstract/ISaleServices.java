package com.stoptakip.dao.daoabstract;

import com.stoptakip.dto.complexmodels.ComplexSale;
import com.stoptakip.dto.complexmodels.ComplexTotalSale;
import com.stoptakip.dto.models.Sale;

import java.util.ArrayList;

public interface ISaleServices {

    boolean insert(Sale entity);
    ArrayList<ComplexSale> getAllComplex();
    ArrayList<ComplexTotalSale> getTotalComplex();
}
