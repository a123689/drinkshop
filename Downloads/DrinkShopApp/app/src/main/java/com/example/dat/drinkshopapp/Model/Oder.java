package com.example.dat.drinkshopapp.Model;

public class Oder {
    private long OrderId;
    private int OderStatus;
    private float OderPrice;
    private String OderDetail,OderComment,OderAddress,UserPhone;

    public Oder(){

    }
    public long getOderId() {
        return OrderId;
    }

    public void setOderId(long oderId) {
        OrderId = oderId;
    }

    public int getOderStatus() {
        return OderStatus;
    }

    public void setOderStatus(int oderStatus) {
        OderStatus = oderStatus;
    }

    public float getOderPrice() {
        return OderPrice;
    }

    public void setOderPrice(float oderPrice) {
        OderPrice = oderPrice;
    }

    public String getOderDetail() {
        return OderDetail;
    }

    public void setOderDetail(String oderDetail) {
        OderDetail = oderDetail;
    }

    public String getOderComment() {
        return OderComment;
    }

    public void setOderComment(String oderComment) {
        OderComment = oderComment;
    }

    public String getOderAddress() {
        return OderAddress;
    }

    public void setOderAddress(String oderAddress) {
        OderAddress = oderAddress;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
