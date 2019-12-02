package com.example.shookisha.entity;

import java.io.Serializable;

public class Offre implements Serializable {
    private int catId;
    private String catLabel;
    private int couId;
    private int cstId;
    private ImgOffer [] imgOffer;
    private String offerDescription;
    private int offerId;
    private double offerNetPrice;
    private double offerPrice;
    private int offerReduction;
    private String offerTitle;
    private int promotionTypeId;
    private ShopOffer[] shopOffer;
    private int userId;
    private boolean inBascket;
    private boolean realLife;
    private boolean onLine;
    private boolean phygital;

    public Offre(){}

    public Offre(int catId, String catLabel, int couId, int cstId, ImgOffer[] imgOffer, String offerDescription, int offerId, double offerNetPrice, double offerPrice, int offerReduction, String offerTitle, int promotionTypeId, ShopOffer[] shopOffer, int userId) {
        this.catId = catId;
        this.catLabel = catLabel;
        this.couId = couId;
        this.cstId = cstId;
        this.imgOffer = imgOffer;
        this.offerDescription = offerDescription;
        this.offerId = offerId;
        this.offerNetPrice = offerNetPrice;
        this.offerPrice = offerPrice;
        this.offerReduction = offerReduction;
        this.offerTitle = offerTitle;
        this.promotionTypeId = promotionTypeId;
        this.shopOffer = shopOffer;
        this.userId = userId;
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

    public int getCouId() {
        return couId;
    }

    public void setCouId(int couId) {
        this.couId = couId;
    }

    public int getCstId() {
        return cstId;
    }

    public void setCstId(int cstId) {
        this.cstId = cstId;
    }

    public ImgOffer[] getImgOffer() {
        return imgOffer;
    }

    public void setImgOffer(ImgOffer[] imgOffer) {
        this.imgOffer = imgOffer;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public double getOfferNetPrice() {
        return offerNetPrice;
    }

    public void setOfferNetPrice(double offerNetPrice) {
        this.offerNetPrice = offerNetPrice;
    }

    public double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(double offerPrice) {
        this.offerPrice = offerPrice;
    }

    public int getOfferReduction() {
        return offerReduction;
    }

    public void setOfferReduction(int offerReduction) {
        this.offerReduction = offerReduction;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public int getPromotionTypeId() {
        return promotionTypeId;
    }

    public void setPromotionTypeId(int promotionTypeId) {
        this.promotionTypeId = promotionTypeId;
    }

    public ShopOffer[] getShopOffer() {
        return shopOffer;
    }

    public void setShopOffer(ShopOffer[] shopOffer) {
        this.shopOffer = shopOffer;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isInBascket() {
        return inBascket;
    }

    public void setInBascket(boolean inBascket) {
        this.inBascket = inBascket;
    }

    public boolean isRealLife() {
        return realLife;
    }

    public void setRealLife(boolean realLife) {
        this.realLife = realLife;
    }

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public boolean isPhygital() {
        return phygital;
    }

    public void setPhygital(boolean phygital) {
        this.phygital = phygital;
    }
}
