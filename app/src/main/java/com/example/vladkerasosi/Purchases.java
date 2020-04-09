package com.example.vladkerasosi;

public class Purchases {
    private String data;
    private int sum;
    private String typeOfPurchases;
    private int imgRes;

    public String getData() {
        return data;
    }



    public void setData(String data) {
        this.data = data;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public Purchases(String data, int sum, String typeOfPurchases, int imgRes) {
        this.data = data;
        this.sum = sum;
        this.typeOfPurchases = typeOfPurchases;
        this.imgRes = imgRes;
    }

    public String getTypeOfPurchases() {
        return typeOfPurchases;
    }

    public void setTypeOfPurchases(String typeOfPurchases) {
        this.typeOfPurchases = typeOfPurchases;
    }


}
