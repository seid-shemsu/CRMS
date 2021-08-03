package com.izhar.crms.objects;

public class Incident {
    String id, date_time, type, description, location, image;

    public Incident() {
    }

    public Incident(String id, String date_time, String type, String description, String location, String image) {
        this.id = id;
        this.date_time = date_time;
        this.type = type;
        this.description = description;
        this.location = location;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getType(){
        return this.type;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }
}
