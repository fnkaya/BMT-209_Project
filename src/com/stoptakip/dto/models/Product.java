package com.stoptakip.dto.models;

public class Product {

    private int id;
    private String barcode_no;
    private String name;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode_no() {
        return barcode_no;
    }

    public void setBarcode_no(String barcode_no) {
        this.barcode_no = barcode_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name;
    }
}
