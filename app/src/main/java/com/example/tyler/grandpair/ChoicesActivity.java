package com.example.tyler.grandpair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class ChoicesActivity extends AppCompatActivity {

    private ImageButton settings;
    private ImageButton profile;
    private ImageButton discover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices);
        settings = (ImageButton) findViewById(R.id.settings);
        profile = (ImageButton) findViewById(R.id.profile);
        discover = (ImageButton) findViewById(R.id.discover);
        int width = getResources().getDisplayMetrics().widthPixels/3;
        int hei=getResources().getDisplayMetrics().heightPixels/3;
        //settings.setLayoutParams(new LinearLayout.LayoutParams(width,hei));
        //profile.setLayoutParams(new LinearLayout.LayoutParams(width,hei));
        //discover.setLayoutParams(new LinearLayout.LayoutParams(width,hei));



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