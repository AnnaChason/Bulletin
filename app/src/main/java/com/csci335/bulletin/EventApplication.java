package com.csci335.bulletin;

public class EventApplication {
    public String date;
    public String title;
    public String description;

    public EventApplication(String date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }

    public EventApplication() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
