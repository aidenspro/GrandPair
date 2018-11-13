package com.example.tyler.grandpair;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

public class EventActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView mImageView;
    private TextView mDate;
    private TextView mLocation;

    private ImageView mPic;
    private TextView mAge;
    private TextView mName;

    private TextView mEventName;
    private ScrollView mScroll;

    private String fName;
    private String lName;
    private String url;
    private String age;
    private int Event_ID;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_event);
        mScroll = (ScrollView) findViewById(R.id.scroll);
        mEventName = (TextView) findViewById(R.id.location);
        mLocation = (TextView) findViewById(R.id.eventName);
        mDate = (TextView) findViewById(R.id.date);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Event_ID = getIntent().getIntExtra("EVENT_ID",0);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.profilemini, null);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(v);

        mScroll.addView(ll);



        mAuth = FirebaseAuth.getInstance();
        String User_id = mAuth.getCurrentUser().getUid();
        mImageView =(ImageView)findViewById(R.id.eventPicture);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference getName = db.getInstance().getReference().child("Users").child(User_id).child("first name");
        DatabaseReference getAge = db.getInstance().getReference().child("Users").child(User_id).child("age");
        final DatabaseReference getFirst = db.getInstance().getReference().child("Event").child(""+Event_ID).child("Name");
        DatabaseReference getLast = db.getInstance().getReference().child("Event").child(""+Event_ID).child("Location");
        DatabaseReference getDate = db.getInstance().getReference().child("Event").child(""+Event_ID).child("Date");
        DatabaseReference getURL = db.getInstance().getReference().child("Event").child(""+Event_ID).child("URL");


        getFirst.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fName = dataSnapshot.getValue(String.class);
                mEventName.setText(fName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                url = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getLast.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lName = dataSnapshot.getValue(String.class);
                mLocation.setText(lName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                age = dataSnapshot.getValue(String.class);
                mDate.setText(age);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mName = (TextView) v.findViewById(R.id.nameMini);
        mAge = (TextView) v.findViewById(R.id.ageNum);
        mPic =(ImageView)v.findViewById(R.id.miniPic);


        getName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fName = dataSnapshot.getValue(String.class);
                mName.setText(fName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getAge.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fName = dataSnapshot.getValue(String.class);
                mAge.setText(fName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        mStorageRef.child("UserPictures").child(User_id + "pic1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                //Drawable drawable = LoadImageFromWebOperations(uri.toString());
                try {
                    URL url = new URL(uri.toString());
                    Glide.with(getApplicationContext()).load(uri).into(mPic);

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

        mStorageRef.child("EventPictures").child(Event_ID + "pic1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                try {
                    URL url = new URL(uri.toString());
                    //uri = getIntent().getStringExtra("CURRENT_URL");
                   // uri = Uri.parse(getIntent().getStringExtra("CURRENT_URL"));
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

    }




}
