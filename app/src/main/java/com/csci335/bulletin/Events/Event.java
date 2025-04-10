package com.csci335.bulletin.Events;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csci335.bulletin.Organizations.Organization;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/*
class to hold data for each event post
 */
public class Event implements Comparable<Event>{
    private String title;
    private String date;
    private String location;
    private String description;
    private int attendance;
    // posterImg is the complete database reference necessary to retrieve the image
    private String posterImg;
    private String category;
    private String organizationID;
    private String organizationName;
 //   private static StorageReference storage = FirebaseStorage.getInstance().getReference();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Event(String title, String date, String location, String description, String posterImg, int attendance, String category, String organizationID) {
        this.location = location;
        this.title = title;
        this.date = date;
        this.description = description;
        this.posterImg = posterImg;
        this.attendance = attendance;
        this.category = category;
        this.organizationID = organizationID;
        if(organizationID != null && !organizationID.equals("")) {
            DocumentReference docRef = db.collection("organizationInfo").document(organizationID);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null) {
                        Organization org = documentSnapshot.toObject(Organization.class);
                        if (org != null) {
                            setOrganizationName(org.getName());
                            db.collection("eventApplications").document(title).update("organizationName", org.getName());
                        }
                    }
                }
            });
        }
    }
    public Event(){

    }

    public static String[] categoryOptions(){
        return new String[]{"", "Sport", "Performance", "Ministry/Service", "Speaker", "Faith", "Movie/Games", "Informational", "Art", "Food", "Academics"};
    }
    /*
    Getters and Setters
     */
    public String getTitle() {
        return title;
    }
    public void setTitle(String title){this.title=title;}
    public String getDate() {return date;}
    public void setDate(String date){this.date =date;}
    public String getLocation() {return location;}
    public void setLocation(String location){this.location = location;}
    public String getDescription() {return description;}
    public void setDescription(String description){this.description = description;}
    public int getAttendance() {return attendance;}
    public void setAttendance(int attendance){this.attendance = attendance;}
    //adds num to number of attendance (just use negative number to take away attendees)
    public void updateAttendance(int num){attendance += num;}
    public String getPosterImg() {return posterImg;}
    public void setPosterImg(String posterImg){this.posterImg = posterImg;}
    public String getCategory() {return category;}
    public void setCategory(String category){this.category = category;}
    public String getOrganizationID() {return organizationID;}
    public void setOrganizationID(String organizationID) {this.organizationID = organizationID;}
    public String getOrganizationName() {return organizationName;}
    public void setOrganizationName(String organizationName) {this.organizationName = organizationName;}

    /*
            Returns event date as a number that can be compared to other dates. form YYMMDD
             */
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

    /*
   gets events from the database
    */
    public static ArrayList<Event> setUpEvents(RecyclerView eventRV){
        ArrayList<Event> events = new ArrayList<>();
        db.collection("approvedEvents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Event event = document.toObject(Event.class);
                                events.add(event);
                            }
                            filterEvents(events);
                            Collections.sort(events);
                            eventRV.getAdapter().notifyDataSetChanged();
                        } else {
                            System.out.println("ERROR RETREIVING EVENT FEED"); //fix later
                        }
                    }
                });
        return events;
    }

    /*
   removes past events
    */
    public static void filterEvents(ArrayList<Event> events){
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        // Get today's date in YYMMDD format
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        String formattedDate = currentDate.format(formatter);
        int[] currentDateArr = dateToNum(formattedDate);  // Convert today's date to YYMMDD
        int todayDateNum = (currentDateArr[2] * 10000) + (currentDateArr[0] * 100) + currentDateArr[1];  // Construct YYMMDD
        // Filter out events that have already happened
        Iterator<Event> iterator = events.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (event.dateToNum() < todayDateNum) {
                iterator.remove();  // Remove event if it's before today's date
                // also delete event from the database and its poster
                if(event.getTitle() != null){
                    db.collection("approvedEvents").document(event.getTitle()).delete();
                    storage.child(event.getPosterImg()).delete();
                }
            }
        }
    }

    /*
    returns int array where index 0 has the month, 1 has the day, and 0 has the last 2 digits of the year
    should get rid of for cleaner code, but that's a later problem
     */
    private static int[] dateToNum(String date){
        int[] dateNums = new int[3];
        try{
            int idx = date.indexOf("/");
            dateNums[0] = Integer.parseInt(date.substring(0, idx));
            dateNums[1] = Integer.parseInt(date.substring(idx+1, date.indexOf("/",idx+1)));
            idx = date.indexOf("/",idx+1);
            dateNums[2] = Integer.parseInt(date.substring(idx+1));
        } catch (Exception e) {
            dateNums = new int[]{-900,0,0};
        }
        return dateNums;
    }

}
