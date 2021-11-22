package com.example.loginactivity;

import java.util.Date;

public class GetItem {

    String itemName, itemDescription, itemLocation, userEmail;
    Date itemDate;

    public GetItem(){}

    public GetItem(String name, String itemDescription, String itemLocation, String userEmail, Date itemDate) {
        this.itemName = name;
        this.itemDescription = itemDescription;
        this.itemLocation = itemLocation;
        this.userEmail = userEmail;
        this.itemDate = itemDate;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getItemName() {
        return itemName;
    }

    public void setName(String name) {
        this.itemName = name;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public Date getItemDate() {
        return itemDate;
    }

    public void setItemDate(Date itemDate) {
        this.itemDate = itemDate;
    }
}
