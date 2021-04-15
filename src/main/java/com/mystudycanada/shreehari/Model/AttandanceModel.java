package com.mystudycanada.shreehari.Model;

public class AttandanceModel {

    String first_name;
    String last_name;
    private String id;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCoaching_reg_no() {
        return coaching_reg_no;
    }

    public void setCoaching_reg_no(String coaching_reg_no) {
        this.coaching_reg_no = coaching_reg_no;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getIs_absent() {
        return is_absent;
    }

    public void setIs_absent(String is_absent) {
        this.is_absent = is_absent;
    }

    String coaching_reg_no;
    String standard;
    String batch;
    String is_absent;

    public void setId(String id) {

        this.id = id;
    }

    public String getId() {
        return id;
    }
}
