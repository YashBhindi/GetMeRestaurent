package com.example.yash.getmerestaurent;

/**
 * Created by yash on 1/4/18.
 */

public class RestaurentBean {

    private String id,RestaurentName,ManagerNamem,ManagerContact,RestaurentAddress,OpenDay,CloseDay,OpenTime,CloseTime;

    public RestaurentBean(){}

    public RestaurentBean(String id, String RestaurentName, String ManagerNamem, String managerContact, String RestaurentAddress, String OpenDay, String CloseDay, String OpenTime, String CloseTime) {
        this.id=id;
        this.RestaurentName = RestaurentName;
        this.ManagerNamem = ManagerNamem;
        this.ManagerContact = managerContact;
        this.RestaurentAddress = RestaurentAddress;
        this.OpenDay = OpenDay;
        this.CloseDay = CloseDay;
        this.OpenTime = OpenTime;
        this.CloseTime = CloseTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurentName() {
        return RestaurentName;
    }

    public void setRestaurentName(String restaurentName) {
        RestaurentName = restaurentName;
    }

    public String getManagerNamem() {
        return ManagerNamem;
    }

    public void setManagerNamem(String managerNamem) {
        ManagerNamem = managerNamem;
    }

    public String getManagerContact() {
        return ManagerContact;
    }

    public void setManagerContact(String managerContact) {
        ManagerContact = managerContact;
    }

    public String getRestaurentAddress() {
        return RestaurentAddress;
    }

    public void setRestaurentAddress(String restaurentAddress) {
        RestaurentAddress = restaurentAddress;
    }

    public String getOpenDay() {
        return OpenDay;
    }

    public void setOpenDay(String openDay) {
        OpenDay = openDay;
    }

    public String getCloseDay() {
        return CloseDay;
    }

    public void setCloseDay(String closeDay) {
        CloseDay = closeDay;
    }

    public String getOpenTime() {
        return OpenTime;
    }

    public void setOpenTime(String openTime) {
        OpenTime = openTime;
    }

    public String getCloseTime() {
        return CloseTime;
    }

    public void setCloseTime(String closeTime) {
        CloseTime = closeTime;
    }
}
