package com.csci335.bulletin.Main;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.csci335.bulletin.R;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CheckBox sportCheckBox = findViewById(R.id.sportCheckBox);
        CheckBox performanceCheckBox = findViewById(R.id.performanceCheckBox);
        CheckBox ministryServiceCheckBox = findViewById(R.id.ministryServiceCheckBox);
        CheckBox speakerCheckBox = findViewById(R.id.speakerCheckBox);
        CheckBox faithCheckBox = findViewById(R.id.faithCheckBox);
        CheckBox movieGamesCheckBox = findViewById(R.id.movieGamesCheckBox);
        CheckBox informationalCheckBox = findViewById(R.id.informationalCheckBox);
        CheckBox artCheckBox = findViewById(R.id.artCheckBox);
        CheckBox foodCheckBox = findViewById(R.id.foodCheckBox);
        CheckBox academicsCheckBox = findViewById(R.id.academicsCheckBox);

        CheckBox[] boxes = {sportCheckBox, performanceCheckBox, ministryServiceCheckBox,
                speakerCheckBox, faithCheckBox, movieGamesCheckBox, informationalCheckBox,
                artCheckBox, foodCheckBox, academicsCheckBox};

        View.OnClickListener checkBoxListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                // add to the list of categories if checked

                // remove from list if unchecked

                // either way, update the recycler view
                // adapter.notifyDatasetChanged();
            }
        };

        for (CheckBox box : boxes) {
            box.setOnClickListener(checkBoxListener);
        }
    }
}