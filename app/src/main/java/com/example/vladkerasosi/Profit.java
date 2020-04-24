package com.example.vladkerasosi;

public class Profit {

    private String date;
    private float sum;
    private String name;
    private String typeOfProfit;

    public Profit(String date, float sum, String name, String typeOfProfit) {
        this.date = date;
        this.sum = sum;
        this.name = name;
        this.typeOfProfit = typeOfProfit;
    }

    public Profit(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeOfProfit() {
        return typeOfProfit;
    }

    public void setTypeOfProfit(String typeOfProfit) {
        this.typeOfProfit = typeOfProfit;
    }
}
