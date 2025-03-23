package com.csci335.bulletin;

public class EventApplication {
    private String date;
    private String title;
    private String description;
    private String location;
    private int posterImg;
    private int attendance;
    private String category;

    public EventApplication(String date, String title, String description, String location, String category) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.location = location;
        this.attendance = 0;
        this.category = category;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPosterImg(){return posterImg;}
    public void setPosterImg(int poserImg){this.posterImg = poserImg;}

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public String getCategory(){return category;}
    public void setCategory(String category){this.category=category;}

    public Event toEvent(){
        return new Event(title,date,location,description,posterImg,attendance,category);
    }
}
