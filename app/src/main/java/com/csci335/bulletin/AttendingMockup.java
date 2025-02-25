package com.csci335.bulletin;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AttendingMockup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attending_mockup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton attendingBtn = findViewById(R.id.attendingButton);
        TextView attendanceCount = findViewById(R.id.attendanceCount);
        attendingBtn.setOnClickListener(new View.OnClickListener() {
            boolean on = false;
            @Override
            public void onClick(View v) {
                // turn yellow
                // if it is already on, turn off
                if (on){
                    attendingBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_off));
                    on = false;
                    // basic change to attendance number
                    attendanceCount.setText("92");
                // if it was off, turn on
                }else{
                    attendingBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),android.R.drawable.btn_star_big_on));
                    on = true;
                    // basic change to attendance number
                    attendanceCount.setText("93");
                }

            }
        });

    }


}