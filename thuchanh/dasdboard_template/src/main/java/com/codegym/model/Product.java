package com.codegym.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private Date createAt;

    public Product() {
    }

    public Product(int id, String name, String description, double price, String createAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.createAt = format.parse(createAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Product(int id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
