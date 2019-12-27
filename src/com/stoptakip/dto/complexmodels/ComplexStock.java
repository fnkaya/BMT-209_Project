package com.stoptakip.dto.complexmodels;

import com.stoptakip.dto.models.Stock;

public class ComplexStock extends Stock {

    private String product_name;
    private String staff_name;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    //Table için nesnenin object dizisi olarak gönderilmesi
    public Object[] getData(){
        Object[] data = {product_name, getAmount() +" Adet", staff_name, getDate()};

        return data;
    }

    @Override
    public String toString() {
        return staff_name +" "+ product_name;
    }
}
