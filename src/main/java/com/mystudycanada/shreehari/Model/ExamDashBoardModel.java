package com.mystudycanada.shreehari.Model;

public class ExamDashBoardModel {
    private String testName;
    private String date;
    private String testTime;
    private String batch;
    private String standard;

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestName() {
        return testName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getBatch() {
        return batch;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getStandard() {
        return standard;
    }
}
