package com.example.woofNwhiskers.model;

public class UserClass {
    private String uid;
    private String Name;
    private String cover;
    private String email;
    private String image;
    private String petType;
    private String phone;
    private String NoOfServices;

    public UserClass(String uid, String name, String cover, String email, String image, String petType, String phone, String noOfServices) {
        this.uid = uid;
        this.Name = name;
        this.cover = cover;
        this.email = email;
        this.image = image;
        this.petType = petType;
        this.phone = phone;
        this. NoOfServices = noOfServices;
    }

    public String getuserid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNoOfServices() {
        return NoOfServices;
    }

    public void setNoOfServices(String noOfServices) {
        NoOfServices = noOfServices;
    }
}
