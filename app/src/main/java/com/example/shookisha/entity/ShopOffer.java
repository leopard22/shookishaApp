package com.example.shookisha.entity;

import java.io.Serializable;

public class ShopOffer implements Serializable {
    private int shopId;
    private String shopLabel;
    private String shopAdress;


    public ShopOffer(){}

    public ShopOffer(int shopId, String shopLabel) {
        this.shopId = shopId;
        this.shopLabel = shopLabel;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopLabel() {
        return shopLabel;
    }

    public void setShopLabel(String shopLabel) {
        this.shopLabel = shopLabel;
    }

    public String getShopAdress() {
        return shopAdress;
    }

    public void setShopAdress(String shopAdress) {
        this.shopAdress = shopAdress;
    }

    @Override
    public String toString() {
        return "ShopOffer{" +
                "shopId=" + shopId +
                ", shopLabel='" + shopLabel + '\'' +
                ", shopAdress='" + shopAdress + '\'' +
                '}';
    }
}
