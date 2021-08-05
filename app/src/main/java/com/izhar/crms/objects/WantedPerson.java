package com.izhar.crms.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class WantedPerson {
    String id,name, sex, color, description, status;
    String image;
    int age;
    double height;
    public WantedPerson(String id, String name, int age, String sex, String color, double height, String description, String status, String image) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.color = color;
        this.height = height;
        this.description = description;
        this.status = status;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public WantedPerson() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
