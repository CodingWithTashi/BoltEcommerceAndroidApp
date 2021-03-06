package com.tectibet.bolt.domain;

import java.io.Serializable;

/**
 * Created by kharag on 04-04-2020.
 */
public class BestSell implements Serializable {
    String description;
    String name;
    String img_url;
    double price;
    int rating;

    public BestSell() {
    }

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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
