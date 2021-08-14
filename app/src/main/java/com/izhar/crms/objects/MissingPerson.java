package com.izhar.crms.objects;

public class MissingPerson {
    String id, name, image, address, sex, date, color, description, status, reporter_name, reporter_phone;
    int age;
    double height;
    public MissingPerson(String id, String name, int age, String address, String sex, String date, String color, double height, String description, String status, String image, String reporter_name, String reporter_phone) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.address = address;
        this.sex = sex;
        this.date = date;
        this.color = color;
        this.height = height;
        this.description = description;
        this.status = status;
        this.image = image;
        this.reporter_name = reporter_name;
        this.reporter_phone = reporter_phone;
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


    public MissingPerson() {
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }


    public String getAddress() {
        return address;
    }


    public String getSex() {
        return sex;
    }

    public String getDate() {
        return date;
    }

    public String getColor() {
        return color;
    }


    public double getHeight() {
        return height;
    }

    public String getDescription() {
        return description;
    }


    public String getStatus() {
        return status;
    }


    public String getReporter_name() {
        return reporter_name;
    }

    public String getReporter_phone() {
        return reporter_phone;
    }
}
