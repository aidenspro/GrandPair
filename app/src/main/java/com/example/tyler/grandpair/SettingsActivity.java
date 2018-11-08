package com.example.tyler.grandpair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    private Button mEventSettings;
    private Button mProfileSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mProfileSettings = (Button) findViewById(R.id.profileSettings);
        mEventSettings = (Button) findViewById(R.id.createEvent);
        mProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, CreateProfile.class);
                startActivity(intent);

                return;

            }
        });
        mEventSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, CreateEventActivity.class);
                startActivity(intent);

                return;

            }
        });


    }

}
