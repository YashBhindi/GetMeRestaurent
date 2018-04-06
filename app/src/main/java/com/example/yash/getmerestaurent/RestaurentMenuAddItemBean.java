package com.example.yash.getmerestaurent;

/**
 * Created by yash on 2/4/18.
 */

public class RestaurentMenuAddItemBean {

    private String name;
    private double price;
    private String category;

    public RestaurentMenuAddItemBean(){}
    public RestaurentMenuAddItemBean(String category, String name, double price) {
        this.name = name;
        this.price = price;
        this.category=category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
