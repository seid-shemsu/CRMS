package com.izhar.crms.objects;

public class Notification {
    String id, date, message, new_;

    public Notification() {
    }

    public Notification(String id, String date, String message, String new_) {
        this.id = id;
        this.date = date;
        this.message = message;
        this.new_ = new_;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getNew_() {
        return new_;
    }
}
