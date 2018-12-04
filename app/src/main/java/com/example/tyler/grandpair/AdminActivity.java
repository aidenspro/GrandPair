package com.example.tyler.grandpair;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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


public class AdminActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView mImageView;
    private TextView mDate;
    private TextView mLocation;
    private int attendNum;


    private TextView mEventName;
    private ScrollView mScroll;

    private String fName;
    private String lName;
    private String url;
    private String age;
    private int Event_ID;
    private int how;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        mScroll = (ScrollView) findViewById(R.id.scroll);
        mEventName = (TextView) findViewById(R.id.location);
        mLocation = (TextView) findViewById(R.id.eventName);

        mStorageRef = FirebaseStorage.getInstance().getReference();


        mAuth = FirebaseAuth.getInstance();

        mImageView = (ImageView) findViewById(R.id.eventPicture);
        FirebaseDatabase db = FirebaseDatabase.getInstance();



        FirebaseDatabase.getInstance().getReference().child("Event")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        final FirebaseDatabase db = FirebaseDatabase.getInstance();
                        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        //View v = inflater.inflate(R.layout.profilemini, null);
                        final LinearLayout ll = new LinearLayout(AdminActivity.this);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            new Thread(new Runnable() {
                                public void run() {

                                    final ImageView mPic;
                                    final TextView mLocation;
                                    final TextView mName;
                                    final Button delete;


                                    //try {

                                    DatabaseReference getName = snapshot.getRef().child("Name");
                                    DatabaseReference getLocation = snapshot.getRef().child("Location");

                                    //for(int i = 0; i < attendNum; i++) {
                                    final View v = inflater.inflate(R.layout.eventmini, null);

                                    mName = (TextView) v.findViewById(R.id.eventName);
                                    // mLocation = (TextView) v.findViewById(R.id.ageNum);
                                    mPic = (ImageView) v.findViewById(R.id.miniPic);
                                    delete = (Button) v.findViewById(R.id.delete);

                                    delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            mStorageRef.child("EventPictures").child(snapshot.getKey() + "pic1.jpg").delete();
                                            snapshot.getRef().removeValue();
                                            finish();
                                            startActivity(getIntent());

                                        }
                                    });


                                    getName.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            fName = dataSnapshot.getValue(String.class);
                                            mName.setText(fName);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            fName = "Removed";
                                            mName.setText(fName);

                                        }
                                    });
                                    getLocation.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            fName = dataSnapshot.getValue(String.class);
                                            //mLocation.setText(fName);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            fName = "User";
                                            //mLocation .setText(fName);
                                        }
                                    });

                                    //if(fName == ""){
                                    //  mStorageRef.child("UserPictures").child(c_User_Id + "pic1.jpg").delete();
                                    //  db.getReference().child("Event").child(snapshot.toString()).removeValue();
                                    //}else {
                                    mStorageRef.child("EventPictures").child(snapshot.getKey() + "pic1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Got the download URL for 'users/me/profile.png'
                                            //Drawable drawable = LoadImageFromWebOperations(uri.toString());
                                            try {
                                                URL url = new URL(uri.toString());
                                                Glide.with(getApplicationContext()).load(uri).into(mPic);
                                                mPic.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        //makeToast(SwipeActivity.this, "Clicked!");
                                                        Intent intent = new Intent(AdminActivity.this, EventActivity.class);
                                                        //intent.putExtra(URL,i);
                                                        intent.putExtra("HOW",1);
                                                        intent.putExtra("EVENT_ID",dataSnapshot.getKey());
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                        startActivity(intent);
                                                        //finish();
                                                        return;
                                                    }
                                                });

                                                //Toast.makeText(Profitivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                                                //mImageView.setImageDrawable(drawable);
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();

                                            }

                                            runOnUiThread(new Runnable() {

                                                @Override
                                                public void run() {

                                                    ll.addView(v);


                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors

                                        }
                                    });
                                    // }


                                    //}catch(Exception e) {

                                    // }

                                }
                            }).start();

                        }
                        mScroll.addView(ll);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }
}
