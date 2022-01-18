package com.github.sohn919.shopping;


public class BasketItem {
    String code;
    String name;
    String image;
    String brand;
    String description;
    String price;
    String check;

    public BasketItem(String code, String name, String image, String brand, String description, String price, String check) {
        this.code = code;
        this.name = name;
        this.image = image;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.check = check;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

