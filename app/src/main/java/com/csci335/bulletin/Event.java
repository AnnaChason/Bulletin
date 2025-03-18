package com.csci335.bulletin;
/*
class to hold data for each event post
 */
public class Event implements Comparable<Event>{
    private String name;
    private String date;
    private String location;
    private String description;
    private int attending;
    private int posterImg;

    public Event(String name, String date, String location, String description, int posterImg) {
        this.location = location;
        this.name = name;
        this.date = date;
        this.description = description;
        this.posterImg = posterImg;
        this.attending = 0;
    }
    public Event(String name, String date, String location, String description) {
        this.location = location;
        this.name = name;
        this.date = date;
        this.description = description;
        this.posterImg = R.drawable.img;//PLACEHOLDER
        this.attending = 0;
    }

    /*
    Getters and Setters
     */
    public String getEventName() {
        return name;
    }
    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public int getAttending() {
        return attending;
    }

    //adds num to number of attending (just use negative number to take away attendees)
    public void updateAttending(int num){
        attending += num;
    }
    public int getPosterImg() {
        return posterImg;
    }


    public int dateToNum(){
        int dateNums = 0;
        try{
            int idx = date.indexOf("/");
            dateNums = Integer.parseInt(date.substring(0, idx)) * 100;
            dateNums += Integer.parseInt(date.substring(idx+1, date.indexOf("/",idx+1)));
            idx = date.indexOf("/",idx+1);
            String year = date.substring(idx+1);
            if(year.length() > 2)
                year = year.substring(year.length() - 2);
            dateNums += Integer.parseInt(year) * 10000;
    } catch (Exception e) {//date invalid
            dateNums = -999;
        }
        return dateNums;
    }


    /*
    comparing events by date earlier to later
        (could come up with other ways to sort it later like number of attendees)
    0 means equal, - number means this event is earlier, positive number means this event is later
     */
    @Override
    public int compareTo(Event e) {
        return Integer.compare(this.dateToNum(), e.dateToNum());
    }
}
