package com.stoptakip.dto.complexmodels;

public class ComplexTotalSale extends ComplexSale{

    private int total_amount;

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public Object[] getData(){
        Object[] data = {getProduct_name(), total_amount +" Adet", getTotal_price() +"₺"};

        return data;
    }

    @Override
    public String toString() {
        return getProduct_name() +" "+ total_amount;
    }
}
