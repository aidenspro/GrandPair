package com.example.tyler.grandpair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChoicesActivity extends AppCompatActivity {

    private Button settings;
    private Button profile;
    private Button discover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices);
        settings = (Button) findViewById(R.id.settings);
        profile = (Button) findViewById(R.id.profile);
        discover = (Button) findViewById(R.id.discover);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoicesActivity.this, ProfileActivity.class);
                startActivity(intent);

                return;

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoicesActivity.this, ProfileActivity.class);
                startActivity(intent);

                return;

            }
        });
        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoicesActivity.this, SwipeActivity.class);
                startActivity(intent);

                return;

            }
        });



    }

}