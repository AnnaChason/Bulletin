package com.csci335.bulletin.Events;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csci335.bulletin.Organizations.Organization;
import com.csci335.bulletin.StudentClasses.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
    private String docId;

    // time data
    private int hour;
    private int minute;

 //   private static StorageReference storage = FirebaseStorage.getInstance().getReference();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Students who have marked themselves as attending this event
    private List<String> students;

    public Event(String title, String date, String location, String description, String posterImg, int attendance, String category, String organizationID, List<String> students, int hour, int minute) {
        this.location = location;
        this.title = title;
        this.date = date;
        this.description = description;
        this.posterImg = posterImg;
        this.attendance = attendance;
        this.category = category;
        this.organizationID = organizationID;
        this.students = students;
        this.hour = hour;
        this.minute = minute;

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
        return new String[]{"Sport", "Performance", "Ministry/Service", "Speaker", "Faith", "Movie/Games", "Informational", "Art", "Food", "Academics"};
    }
    /*
    Getters and Setters
     */
    public String getDocId() {
        return docId;
    }
    public void setDocId(String docId) {
        this.docId = docId;
    }
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
    public int getAttendance() {if(students != null) return students.size(); else return 0;}
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
    public List<String> getStudents() { return this.students; }
    public void setStudents(List<String> students) { this.students = students; }
    public int getHour() {return hour;}
    public void setHour(int hour) {this.hour = hour;}
    public int getMinute() {return minute;}
    public void setMinute(int minute) {this.minute = minute;}

    public void addStudent(String student) {
        if(students == null) {
            students = new ArrayList<String>();
        }
        this.students.add(student);
    }
    public void removeStudent(String student) {
        this.students.remove(student);
    }

    /*
    public String studentsToString() {
        String result = "";
        for(int i = 0; i < students.toArray().length; i++) {
            result += students.get(i).getName() + " ";
        }
        return result;
    }*/


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
   moves past events to archive
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
                // move event to archive
                if(event.getTitle() != null){
                    db.collection("eventArchive").document(event.getTitle()).set(event);
                    db.collection("approvedEvents").document(event.getTitle()).delete();
                    db.collection("eventApplications").document(event.getTitle()).delete();
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
    0 means equal, - number means this event is earlier, positive number means this event is later
    */
    @Override
    public int compareTo(Event e) {
        // compare the dates first and if they are equal compare the hours then minutes
        if (Integer.compare(this.dateToNum(), e.dateToNum()) == 0){
            if (Integer.compare(this.getHour(), e.getHour()) == 0) {
                return Integer.compare(this.getMinute(), e.getMinute());
            }
            else return Integer.compare(this.getHour(), e.getHour());
        }
        else return Integer.compare(this.dateToNum(), e.dateToNum());
    }


    /*
    compares events by number of people attending
     */
    private static class PopularityFilter implements Comparator<Event> {
        @Override
        public int compare(Event e1, Event e2) {
            return Integer.compare(e2.getAttendance(), e1.getAttendance());
        }
    }
    /*
    Compares events by their date
     */
    private static class DateFilter implements Comparator<Event>{
        @Override
        public int compare(Event e1, Event e2) {
            return e1.compareTo(e2);
        }
    }
    private static class AttendFilter implements Comparator<Event>{
        @Override
        public int compare(Event o1, Event o2) {
            FirebaseAuth fauth = FirebaseAuth.getInstance();
            boolean attend1 = o1.getStudents().contains(fauth.getCurrentUser().getUid());
            boolean attend2 = o2.getStudents().contains(fauth.getCurrentUser().getUid());
            if(attend1 && !attend2){
                return -1;
            }
            if(attend1 && attend2){//if attending both sort by date
                DateFilter df = new DateFilter();
                return df.compare(o1,o2);
            }
            if(attend2){
                return 1;
            }
            return 0;
        }
    }
    private static class FollowFilter implements Comparator<Event>{
        @Override
        public int compare(Event o1, Event o2) {
            return 0;
        }
    }
    public static String[] sortTypes(){
        return new String[] {"Date", "Popularity", "Attending","Followed Organizations"};
    }
    public static Comparator<Event> sortMethods(int i){
        Comparator[] methods = {new DateFilter(),new PopularityFilter(), new AttendFilter(), new FollowFilter()};
        return methods[i];
    }
}
