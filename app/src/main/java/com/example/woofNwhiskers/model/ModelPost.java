package com.example.woofNwhiskers.model;

public class ModelPost {
    String uid, uName, uEmail, uDp, pId, pLikes, pTitle, pType, pLocation, pDescription, pImage, pTime;



    public ModelPost() {

    }
public ModelPost(String uid,String uName,String uEmail,String uDp,String pId,String pLikes,String pTitle,String pType,String pLocation,String pDescription,String pImage,String pTime){
    this.uid=uid;
    this.uName=uName;
    this.uEmail=uEmail;
    this.uDp=uDp;
    this.pId=pId;
    this.pLikes=pLikes;
    this.pTitle=pTitle;
    this.pType=pType;
    this.pLocation=pLocation;
    this.pDescription=pDescription;
    this.pImage=pImage;
    this.pTime=pTime;


}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpLikes() {
        return pLikes;
    }

    public void setpLikes(String pLikes) {
        this.pId = pLikes;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getpLocation() {
        return pLocation;
    }

    public void setpLocation(String pLocation) {
        this.pLocation = pLocation;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }
}

