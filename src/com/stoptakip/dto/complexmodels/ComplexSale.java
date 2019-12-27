package com.stoptakip.dto.complexmodels;

import com.stoptakip.dto.models.Sale;

public class ComplexSale extends Sale {

    private String product_name;
    private String customer_name;
    private String staff_name;
    private double price;
    private double total_price;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    //Table için nesnenin object dizisi olarak gönderilmesi
    public Object[] getData(){
        Object[] data = {customer_name, product_name, price +" ₺", getAmount() +" Adet", total_price +"₺", staff_name, getDate()};

        return data;
    }

    @Override
    public String toString() {
        return product_name +" "+ customer_name +" "+ staff_name;
    }
}
