package com.example.shreehari.Model;

public class StudentModel {

    String mobile_user_master_id;
    String batch;
    String standard;
    String coaching_reg_no;
    String first_name;
    private String selected;

    public String getMobile_user_master_id() {
        return mobile_user_master_id;
    }

    public void setMobile_user_master_id(String mobile_user_master_id) {
        this.mobile_user_master_id = mobile_user_master_id;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getCoaching_reg_no() {
        return coaching_reg_no;
    }

    public void setCoaching_reg_no(String coaching_reg_no) {
        this.coaching_reg_no = coaching_reg_no;
    }

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

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    String last_name;
    String profile_pic;


    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getSelected() {
        return selected;
    }
}
