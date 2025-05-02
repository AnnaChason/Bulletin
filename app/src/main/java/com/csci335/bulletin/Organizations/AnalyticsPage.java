package com.csci335.bulletin.Organizations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsPage extends AppCompatActivity {

    private Button backBtn;
    private int female = 0;
    private int male = 0;
    private int freshmen = 0;
    private int sophomore = 0;
    private int junior = 0;
    private int senior = 0;
    private int grad = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analytics_page);

        //findViews
        PieChart pieChart = findViewById(R.id.pieChart);
        TextView chartTitle = findViewById(R.id.chartTitle);
        TextView followerText = findViewById(R.id.followerText);
        TextView followerCount = findViewById(R.id.followerCount);
        Spinner chartSelector = findViewById(R.id.chartSelector);
        ImageButton backBtn = findViewById(R.id.backBtn);

        // Load follower count
        getNumFollowers(count -> {
            followerCount.setText(String.valueOf(count));
        });

        backBtn.setOnClickListener(v -> {
            Intent homeIntent = new Intent(this, OrganizationProfilePage.class);
            startActivity(homeIntent);
            finish();
        });

        //drop down options
        String[] chartOptions = {"By Gender", "By Year"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, chartOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chartSelector.setAdapter(adapter);

        //What happens when you select diffrent drop options
        chartSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    chartTitle.setText("User Gender Distribution");
                    loadGenderChart(pieChart);
                } else if (position == 1) {
                    chartTitle.setText("User Year Group Distribution");
                    loadAgeChart(pieChart);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getNumFollowers(FollowerNumm callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String orgID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference orgRef = db.collection("organizationInfo").document(orgID);

        orgRef.get().addOnSuccessListener(snapshot -> {
            int count = 0;
            if (snapshot.exists()) {
                List<String> followerIds = (List<String>) snapshot.get("followers");
                if (followerIds != null) {
                    count = followerIds.size();
                }
            }
            callback.onCountReady(count);
        });
    }

    private void loadGenderChart(PieChart pieChart) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String orgID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference orgRef = db.collection("organizationInfo").document(orgID);

        orgRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                List<String> followerIds = (List<String>) snapshot.get("followers");
                if (followerIds != null && !followerIds.isEmpty()) {
                    int total = followerIds.size();
                    final int[] loaded = {0};

                    for (String uid : followerIds) {
                        db.collection("studentInfo").document(uid).get().addOnSuccessListener(doc -> {
                            if (doc.exists()) {
                                String gender = doc.getString("gender");
                                if (gender != null) {
                                    if (gender.equalsIgnoreCase("female")) {
                                        female++;
                                    } else if (gender.equalsIgnoreCase("male")) {
                                        male++;
                                    }
                                }
                            }

                            loaded[0]++;
                            if (loaded[0] == total) {
                                List<PieEntry> entries = new ArrayList<>();
                                if (female > 0) entries.add(new PieEntry(female, "Female"));
                                if (male > 0) entries.add(new PieEntry(male, "Male"));
                                updateChart(pieChart, entries, "User Gender");
                                // All documents loaded – now update chart
                            }
                        });
                    }
                }
            }
        });
    }

    private void loadAgeChart(PieChart pieChart) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String orgID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference orgRef = db.collection("organizationInfo").document(orgID);

        orgRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                List<String> followerIds = (List<String>) snapshot.get("followers");
                if (followerIds != null && !followerIds.isEmpty()) {
                    int total = followerIds.size();
                    final int[] loaded = {0};

                    for (String uid : followerIds) {
                        db.collection("studentInfo").document(uid).get().addOnSuccessListener(doc -> {
                            if (doc.exists()) {
                                String year = doc.getString("year");
                                if (year != null) {
                                    if (year.equalsIgnoreCase("freshmen")) {
                                        freshmen++;
                                    } else if (year.equalsIgnoreCase("sophomore")) {
                                        sophomore++;
                                    } else if (year.equalsIgnoreCase("senior")){
                                        senior++;
                                    } else if (year.equalsIgnoreCase("junior")) {
                                        junior++;
                                    } else {
                                        grad++;
                                    }
                                }
                            }

                            loaded[0]++;
                            if (loaded[0] == total) {
                                List<PieEntry> entries = new ArrayList<>();
                                if (freshmen > 0) entries.add(new PieEntry(freshmen, "Freshmen"));
                                if (sophomore > 0) entries.add(new PieEntry(sophomore, "Sophomore"));
                                if (junior > 0) entries.add(new PieEntry(junior, "Junior"));
                                if (senior > 0) entries.add(new PieEntry(senior, "Senior"));
                                if (grad > 0) entries.add(new PieEntry(grad, "Grad"));
                                updateChart(pieChart, entries, "User Year");
                                // All documents loaded – now update chart
                            }
                        });
                    }
                }
            }
        });
    }

    private void updateChart(PieChart pieChart, List<PieEntry> entries, String label) {
       PieDataSet dataSet = new PieDataSet(entries, label);
        //dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setColors(ContextCompat.getColor(getApplicationContext(), R.color.medium_blue),ContextCompat.getColor(getApplicationContext(), R.color.light_orange),ContextCompat.getColor(getApplicationContext(), R.color.light_green),ContextCompat.getColor(getApplicationContext(), R.color.navy_blue),ContextCompat.getColor(getApplicationContext(), R.color.orange),ContextCompat.getColor(getApplicationContext(), R.color.muted_red));
        dataSet.setValueTextSize(0f);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setEntryLabelTextSize(18f);
        pieChart.invalidate(); // refresh
    }
}
