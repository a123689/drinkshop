package com.example.dat.drinkshopapp.Model;

public class OrderResult {
    public int OrderId;
    public String OrderDate;
    public int OderStatus;
    public float OderPrice;
    public String OderDetail;
    public String OderComment;
    public String OderAddress;
    public String UserPhone;

    public OrderResult() {
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
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
