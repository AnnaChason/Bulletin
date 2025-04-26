package com.csci335.bulletin.Main;



import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.csci335.bulletin.AdminClasses.AdminHomePage;
import com.csci335.bulletin.Events.HomePage;
import com.csci335.bulletin.Organizations.EventApplicationForm;
import com.csci335.bulletin.R;
import com.csci335.bulletin.StudentClasses.Search;
import com.csci335.bulletin.Organizations.OrganizationProfilePage;
import com.google.android.material.navigation.NavigationBarView;
/*
 This class makes creating the bottom navigation bars more simple by creating the onItem selected Listener
 and determining what each icon should do based on user type.
 */
public class NavigationManager {
    private NavigationBarView navBar;
    private Context context;
    private Class home, profile, other;

    /*
    sets up given navigation bar with activities based on current user type
     */
    public NavigationManager(NavigationBarView navBar, Context context ){
        this.navBar = navBar;
        this.context = context;
        setClasses();

        navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Class target = null;
                if(R.id.profile == itemId){
                    target = profile;
                }
                else if(R.id.home == itemId){
                    target = home;
                }
                else if(R.id.other == itemId){
                    target = other;
                }
                // Only navigate if it's not the current activity
                if (target != null && !context.getClass().equals(target)) {
                    Intent next = new Intent(context, target);
                    context.startActivity(next);
                    return true;
                }
                return false;
            }
        });
    }
    public NavigationManager(){};

    /*
    finds user type and determines which activities and icons go with it
     */
    private void setClasses(){
        Menu menu = navBar.getMenu();
        MenuItem oItem = menu.findItem(R.id.other);

        //assumes user loading has been run sucessfully
        int userType = UserLoadingScreen.getCurrentUserType();
        switch(userType){
            case 1://admin
                home = AdminHomePage.class;
                profile = Profile.class;
                other = HomePage.class;
                oItem.setIcon(R.drawable.event_feed_vector);
                oItem.setTitle("Event Feed");
                break;
            case 2://organization
                home = EventApplicationForm.class;
                menu.findItem(R.id.home).setIcon(R.drawable.baseline_add_box_24);
                menu.findItem(R.id.home).setTitle("New Event");
                profile = OrganizationProfilePage.class;
                other = Profile.class;
                oItem.setIcon(R.drawable.baseline_settings_24);
                oItem.setTitle("Account Settings");
                break;
            case 3://student
                home = HomePage.class;
                profile = Profile.class;
                other = Search.class;
                oItem.setTitle("Search");
                break;
            default:
                home = HomePage.class;
                profile = Profile.class;
                other = Search.class;
                break;
        }
        //navBar.setItemIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.tan)));
    }


    /*
    returns the current user type's home activity
     */
    public Class getHome(){
        setClasses();
        return home;
    }
}
