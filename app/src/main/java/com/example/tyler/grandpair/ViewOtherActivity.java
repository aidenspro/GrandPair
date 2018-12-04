package com.example.tyler.grandpair;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.MalformedURLException;
import java.net.URL;

public class ViewOtherActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView mAge;
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mBio;
    private Button sendMessage;
    private String fName;
    private String lName;
    private String age;
    private String bio;
    private String USER_ID;
    private String User_id;
    private StorageReference mStorageRef;
    private int messageNum;
    private int messageNum2;
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other);
        mFirstName = (TextView) findViewById(R.id.firstName);
        mLastName = (TextView) findViewById(R.id.lastName);
        mAge = (TextView) findViewById(R.id.age);
        mBio = (TextView) findViewById(R.id.editText4);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        USER_ID = getIntent().getStringExtra("USER_ID");
        sendMessage = (Button) findViewById(R.id.sendMessage);
        mImageView =(ImageView)findViewById(R.id.profilePicture);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        User_id = mAuth.getCurrentUser().getUid();
        final DatabaseReference getFirst = db.getInstance().getReference().child("Users").child(USER_ID).child("first name");
        DatabaseReference getLast = db.getInstance().getReference().child("Users").child(USER_ID).child("last name");
        DatabaseReference getAge = db.getInstance().getReference().child("Users").child(USER_ID).child("age");
        DatabaseReference getBio = db.getInstance().getReference().child("Users").child(USER_ID).child("bio");
        DatabaseReference current_user_db = db.getInstance().getReference().child("Users").child(USER_ID).child("Messages").child(User_id).child("Messages");
        DatabaseReference other_user_db = db.getInstance().getReference().child("Users").child(User_id).child("Messages").child(USER_ID).child("Messages");


        current_user_db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    messageNum = dataSnapshot.child("MessageNum").getValue(Integer.class);
                }catch (Exception e){
                    DatabaseReference current_user_db = db.getInstance().getReference().child("Users").child(USER_ID).child("Messages").child(User_id).child("Messages");
                    current_user_db.child("MessageNum").setValue(0);
                    DatabaseReference other_user_db = db.getInstance().getReference().child("Users").child(User_id).child("Messages").child(USER_ID).child("Messages");
                    other_user_db.child("MessageNum").setValue(0);
                    messageNum = 0;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                messageNum = 0;
            }
        });




        getFirst.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fName = dataSnapshot.getValue(String.class);
                mFirstName.setText(fName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getLast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lName = dataSnapshot.getValue(String.class);
                mLastName.setText(lName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getAge.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                age = dataSnapshot.getValue(String.class);
                mAge.setText(age);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getBio.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bio = dataSnapshot.getValue(String.class);
                mBio.setText(bio);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mStorageRef.child("UserPictures").child(USER_ID + "pic1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                //Drawable drawable = LoadImageFromWebOperations(uri.toString());
                try {
                    URL url = new URL(uri.toString());
                    Glide.with(getApplicationContext()).load(uri).into(mImageView);

                    //Toast.makeText(Profitivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                    //mImageView.setImageDrawable(drawable);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewOtherActivity.this, MessageActivity.class);
                intent.putExtra("USER_ID",USER_ID);
                intent.putExtra("MESSAGE_NUM",messageNum);

                startActivity(intent);

                return;
            }
        });



    }




}
