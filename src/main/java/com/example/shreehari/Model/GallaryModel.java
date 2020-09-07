package com.example.shreehari.Model;

public class GallaryModel {

    String mobile_gallery_category_id;
    String category_name;
    String category_image;
    private String selection;

    public String getMobile_gallery_category_id() {
        return mobile_gallery_category_id;
    }

    public void setMobile_gallery_category_id(String mobile_gallery_category_id) {
        this.mobile_gallery_category_id = mobile_gallery_category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getSelection() {
        return selection;
    }
}
