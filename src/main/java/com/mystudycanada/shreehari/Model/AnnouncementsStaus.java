package com.mystudycanada.shreehari.Model;

public class AnnouncementsStaus {
    String name;
    String txt_latter;
    String batch;
    private String coachnumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTxt_latter() {
        return txt_latter;
    }

    public void setTxt_latter(String txt_latter) {
        this.txt_latter = txt_latter;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    String number;

    public void setCoachnumber(String coachnumber) {
        this.coachnumber = coachnumber;
    }

    public String getCoachnumber() {
        return coachnumber;
    }
}
