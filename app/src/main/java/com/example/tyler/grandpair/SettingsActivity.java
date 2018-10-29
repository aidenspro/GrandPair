package com.example.tyler.grandpair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {


    private TextView mProfileSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mProfileSettings = (Button) findViewById(R.id.profileSettings);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, CreateProfile.class);
                startActivity(intent);

                return;

            }
        });


    }

}
