package com.example.shookisha.entity;

import java.io.Serializable;

public class ResultFilter implements Serializable {
    private int catId;
    private String catLabel;

    public ResultFilter() {
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCatLabel() {
        return catLabel;
    }

    public void setCatLabel(String catLabel) {
        this.catLabel = catLabel;
    }

    @Override
    public String toString() {
        return "ResultFilter{" +
                "catId=" + catId +
                ", catLabel='" + catLabel + '\'' +
                '}';
    }
}
