package com.nehad.winmarkettrackingapplication.data;

import java.util.List;

public class Item {
    private String barcode ;
    private String Description;
    private String price;
    private Boolean Availability ;

    public Item(String barcode, String description, String price, Boolean availability) {
        this.barcode = barcode;
        Description = description;
        this.price = price;
        Availability = availability;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getAvailability() {
        return Availability;
    }

    public void setAvailability(Boolean availability) {
        Availability = availability;
    }
}
