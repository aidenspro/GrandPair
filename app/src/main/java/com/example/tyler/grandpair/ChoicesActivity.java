package com.example.tyler.grandpair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

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
                finish();
                return;

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoicesActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });
        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoicesActivity.this, SwipeActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });



    }

}