package com.example.vladkerasosi;

import java.io.Serializable;

public class Purchases extends ObjectSerializer implements Serializable {
    private String data;
    private float sum;
    private String typeOfPurchases;
    private int imgRes;
    private String name;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public Purchases() {
    }

    public Purchases(String data, float sum, String typeOfPurchases, String name, String description) {
        this.data = data;
        this.sum = sum;
        this.typeOfPurchases = typeOfPurchases;
        this.name = name;
        this.description = description;
    }

    public String getTypeOfPurchases() {
        return typeOfPurchases;
    }

    public void setTypeOfPurchases(String typeOfPurchases) {
        this.typeOfPurchases = typeOfPurchases;
    }


}
