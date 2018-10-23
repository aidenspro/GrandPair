package com.example.tyler.grandpair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {

    private Button mProfile;
    private EditText mFirstName, mLastName, mAge;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fbListener;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_reg);
        mAuth = FirebaseAuth.getInstance();
        mProfile = (Button) findViewById(R.id.saveProfile);
        mFirstName = (EditText) findViewById(R.id.firstName);
        mLastName = (EditText) findViewById(R.id.lastName);
        mAge = (EditText) findViewById(R.id.age);

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = mFirstName.getText().toString();
                final String lastName = mLastName.getText().toString();
                final String age = mAge.getText().toString();


                String User_id = mAuth.getCurrentUser().getUid();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference current_user_db = db.getInstance().getReference().child("Users").child(User_id);

                Map newPost = new HashMap();
                newPost.put("first name", firstName);
                newPost.put("last name", lastName);
                newPost.put("age", age);

                current_user_db.setValue(newPost);


finish();

            }
                }

                );




    }
}
