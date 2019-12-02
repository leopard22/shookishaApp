package com.example.shookisha.entity;

import java.io.Serializable;

public class Infos implements Serializable {
   private int idInfos;
    private String imageInfos;
    private String labelInfos;

    public int getIdInfos() {
        return idInfos;
    }

    public void setIdInfos(int idInfos) {
        this.idInfos = idInfos;
    }

    public String getImageInfos() {
        return imageInfos;
    }

    public void setImageInfos(String imageInfos) {
        this.imageInfos = imageInfos;
    }

    public String getLabelInfos() {
        return labelInfos;
    }

    public void setLabelInfos(String labelInfos) {
        this.labelInfos = labelInfos;
    }
}
