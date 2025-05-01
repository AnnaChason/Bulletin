package com.csci335.bulletin.Organizations;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsPage extends AppCompatActivity {

    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analytics_page);

        //findViews
        PieChart pieChart = findViewById(R.id.pieChart);
        TextView chartTitle = findViewById(R.id.chartTitle);
        Spinner chartSelector = findViewById(R.id.chartSelector);
        Button backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> {
            Intent homeIntent = new Intent(this, OrganizationProfilePage.class);
            startActivity(homeIntent);
            finish();
        });

        //drop down options
        String[] chartOptions = {"By Gender", "By Year"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, chartOptions);
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
                    chartTitle.setText("User Age Group Distribution");
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

    private void loadGenderChart(PieChart pieChart) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(60f, "Female"));
        entries.add(new PieEntry(40f, "Male"));
        updateChart(pieChart, entries, "User Gender");
    }

    private void loadAgeChart(PieChart pieChart) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(25f, "18–24"));
        entries.add(new PieEntry(50f, "25–34"));
        entries.add(new PieEntry(25f, "35+"));
        updateChart(pieChart, entries, "Age Groups");
    }

    private void updateChart(PieChart pieChart, List<PieEntry> entries, String label) {
        PieDataSet dataSet = new PieDataSet(entries, label);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.invalidate(); // refresh
    }
}
