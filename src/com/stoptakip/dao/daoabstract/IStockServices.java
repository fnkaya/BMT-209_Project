package com.stoptakip.dao.daoabstract;

import com.stoptakip.dto.complexmodels.ComplexStock;
import com.stoptakip.dto.complexmodels.ComplexTotalStock;
import com.stoptakip.dto.models.Stock;

import java.util.ArrayList;

public interface IStockServices {

    boolean insert(Stock entity);

    ComplexTotalStock getComplex(int id);
    ArrayList<ComplexStock> getAllComplex();
    ArrayList<ComplexTotalStock> getTotalComplex();
    int getSaleAmount(int product_id);
}
