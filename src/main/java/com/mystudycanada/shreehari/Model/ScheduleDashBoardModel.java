package com.mystudycanada.shreehari.Model;

public class ScheduleDashBoardModel {

    String mobile_schedule_id;
    String subject;
    String class_room;
    String date;
    String start_time;

    public String getMobile_schedule_id() {
        return mobile_schedule_id;
    }

    public void setMobile_schedule_id(String mobile_schedule_id) {
        this.mobile_schedule_id = mobile_schedule_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getClass_room() {
        return class_room;
    }

    public void setClass_room(String class_room) {
        this.class_room = class_room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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

    String end_time;
    String batch;
    String standard;


}
