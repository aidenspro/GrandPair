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
import android.widget.Toast;

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

public class MessageActivity extends AppCompatActivity {
    private String fName;
    private String lName;
    private String url;
    private String age;
    private int Event_ID;
    private int how;
    private FirebaseAuth mAuth;
    private ImageView mImageView;
    private TextView mDate;
    private TextView mLocation;
    private int attendNum;
    private TextView mEventName;
    private ScrollView mScroll;
    private StorageReference mStorageRef;
    private String User_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mScroll = (ScrollView) findViewById(R.id.scroll);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        attendNum = getIntent().getIntExtra("ATTEND_NUM", 0);
        how = getIntent().getIntExtra("HOW", 0);
        User_Id = getIntent().getStringExtra("USER_ID");

        mAuth = FirebaseAuth.getInstance();

        mImageView = (ImageView) findViewById(R.id.eventPicture);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().getReference().child("Users").child(User_Id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        final FirebaseDatabase db = FirebaseDatabase.getInstance();
                        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        final LinearLayout ll = new LinearLayout(MessageActivity.this);
                        ll.setOrientation(LinearLayout.VERTICAL);

                            new Thread(new Runnable() {
                                public void run() {

                                    final ImageView mPic;
                                    final TextView mAge;
                                    final TextView mName;


                                    final String c_User_Id;

                                    c_User_Id = User_Id;
                                    DatabaseReference getName = db.getInstance().getReference().child("Users").child(c_User_Id).child("first name");
                                    DatabaseReference getAge = db.getInstance().getReference().child("Users").child(c_User_Id).child("age");


                                    final View v = inflater.inflate(R.layout.profilemini, null);

                                    mName = (TextView) v.findViewById(R.id.nameMini);
                                    mAge = (TextView) v.findViewById(R.id.ageNum);
                                    mPic = (ImageView) v.findViewById(R.id.miniPic);


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
                                    getAge.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            fName = dataSnapshot.getValue(String.class);
                                            mAge.setText(fName);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            fName = "User";
                                            mAge.setText(fName);
                                        }
                                    });


                                    mStorageRef.child("UserPictures").child(c_User_Id + "pic1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            try {
                                                URL url = new URL(uri.toString());
                                                Glide.with(getApplicationContext()).load(uri).into(mPic);

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
                                            Toast.makeText(MessageActivity.this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                            FirebaseDatabase.getInstance().getReference().child("Event").child(""+Event_ID).child("Attending").child(dataSnapshot.getKey()).removeValue();
                                            try {
                                                mStorageRef.child("UserPictures").child(c_User_Id + "pic1.jpg").delete();
                                            }catch (Exception e){

                                            }
                                        }
                                    });

                                }
                            }).start();


                        mScroll.addView(ll);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}

