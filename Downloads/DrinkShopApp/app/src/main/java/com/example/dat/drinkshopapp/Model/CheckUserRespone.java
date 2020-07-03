package com.example.dat.drinkshopapp.Model;

public class CheckUserRespone {
    private boolean  check;

    private String error_msg;

    public CheckUserRespone(){

    }

    public boolean isExsits() {
        return  check;
    }

    public void setExsits(boolean exsits) {
        this.check = exsits;
    }

    public String getErro_msg() {
        return error_msg;
    }

    public void setErro_msg(String erro_msg) {
        this.error_msg = erro_msg;
    }
}
