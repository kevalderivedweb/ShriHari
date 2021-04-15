package com.mystudycanada.shreehari.Model;

public class AnnouncementModel {

    String title;
    String description;
    String send_by;
    private String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSend_by() {
        return send_by;
    }

    public void setSend_by(String send_by) {
        this.send_by = send_by;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
