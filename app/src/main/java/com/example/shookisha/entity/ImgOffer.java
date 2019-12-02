package com.example.shookisha.entity;

import java.io.Serializable;

public class ImgOffer implements Serializable {
    private int offerImageId;
    private String offerImageLabel;

    public ImgOffer(){}

    public ImgOffer(int offerImageId, String offerImageLabel) {
        this.offerImageId = offerImageId;
        this.offerImageLabel = offerImageLabel;
    }

    public int getOfferImageId() {
        return offerImageId;
    }

    public void setOfferImageId(int offerImageId) {
        this.offerImageId = offerImageId;
    }

    public String getOfferImageLabel() {
        return offerImageLabel;
    }

    public void setOfferImageLabel(String offerImageLabel) {
        this.offerImageLabel = offerImageLabel;
    }

    @Override
    public String toString() {
        return "{\"offerImageId\"=" + offerImageId +", offerImageLabel='" + offerImageLabel + '\'' +'}';
    }
}
