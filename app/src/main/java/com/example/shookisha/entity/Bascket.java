package com.example.shookisha.entity;

import java.io.Serializable;

public class Bascket implements Serializable {
    private String offerImageLabel;
    private int operationCstId;
    private long operationSelectionDate;
    private String couponSku;
    private String couponImageSku;

    public Bascket() {
    }

    public String getOfferImageLabel() {
        return offerImageLabel;
    }

    public void setOfferImageLabel(String offerImageLabel) {
        this.offerImageLabel = offerImageLabel;
    }

    public int getOperationCstId() {
        return operationCstId;
    }

    public void setOperationCstId(int operationCstId) {
        this.operationCstId = operationCstId;
    }

    public long getOperationSelectionDate() {
        return operationSelectionDate;
    }

    public void setOperationSelectionDate(long operationSelectionDate) {
        this.operationSelectionDate = operationSelectionDate;
    }

    public String getCouponSku() {
        return couponSku;
    }

    public void setCouponSku(String couponSku) {
        this.couponSku = couponSku;
    }

    public String getCouponImageSku() {
        return couponImageSku;
    }

    public void setCouponImageSku(String couponImageSku) {
        this.couponImageSku = couponImageSku;
    }
}
