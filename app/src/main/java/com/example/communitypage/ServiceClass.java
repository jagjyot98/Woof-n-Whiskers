package com.example.communitypage;


public class ServiceClass {
    private String userID;
    private String serviceID;
    private String location;
    private String date;
    private String serviceType;
    private String petType;
    private String serviceDesc;
    public ServiceClass(){}
    public ServiceClass(String userID, String serviceID, String location, String date, String serviceType, String petType, String serviceDesc) {    //Date date,
        this.serviceID = serviceID;
        this.userID = userID;
        this.location = location;
        this.date = date;
        this.serviceType = serviceType;
        this.petType = petType;
        this.serviceDesc = serviceDesc;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }
}
