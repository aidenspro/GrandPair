package com.example.tyler.grandpair;

import android.content.Context;
import android.content.Intent;
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
import java.util.concurrent.ConcurrentHashMap;

public class EventActivity extends AppCompatActivity {
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
    private boolean show;
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
        attendNum = getIntent().getIntExtra("ATTEND_NUM",0);
        how = getIntent().getIntExtra("HOW",0);
        Event_ID = getIntent().getIntExtra("EVENT_ID",0);

        mAuth = FirebaseAuth.getInstance();
        final String User_id = mAuth.getCurrentUser().getUid();
        mImageView =(ImageView)findViewById(R.id.eventPicture);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference getName = db.getInstance().getReference().child("Users").child(User_id).child("first name");
        DatabaseReference getAge = db.getInstance().getReference().child("Users").child(User_id).child("age");
        final DatabaseReference getFirst = db.getInstance().getReference().child("Event").child(""+Event_ID).child("Name");
        DatabaseReference getLast = db.getInstance().getReference().child("Event").child(""+Event_ID).child("Location");
        DatabaseReference getDate = db.getInstance().getReference().child("Event").child(""+Event_ID).child("Date");
        DatabaseReference getURL = db.getInstance().getReference().child("Event").child(""+Event_ID).child("URL");

        DatabaseReference getNum = db.getInstance().getReference().child("Event").child(""+Event_ID).child("AttendNum");
        int fAttend = 0;

        if (how == 0) {
            show = true;
            FirebaseDatabase.getInstance().getReference().child("Event").child(""+Event_ID).child("Attending")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if(snapshot.child("User").getValue(String.class).equals(User_id))
                                    show = false;
                            }
                            if(show){
                                try {
                                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                                    DatabaseReference getNum = db.getInstance().getReference().child("Event").child(""+Event_ID).child("AttendNum");
                                    DatabaseReference addAttendee = db.getInstance().getReference().child("Event").child("" + Event_ID).child("Attending").child(""+ attendNum);
                                    int fAttend = 0;
                                    fAttend = attendNum + 1;
                                    getNum.setValue(fAttend);

                                    ConcurrentHashMap newPost = new ConcurrentHashMap();
                                    newPost.put("User", User_id);
                                    addAttendee.setValue(newPost);


                                } catch (Exception e) {

                                }
                            }

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }


                    });



        }

        getNum.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                attendNum = dataSnapshot.getValue(Integer.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


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

        FirebaseDatabase.getInstance().getReference().child("Event").child(""+Event_ID).child("Attending")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        final FirebaseDatabase db = FirebaseDatabase.getInstance();
                        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        //View v = inflater.inflate(R.layout.profilemini, null);
                        final LinearLayout ll = new LinearLayout(EventActivity.this);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            new Thread(new Runnable() {
                                public void run() {

                                    final ImageView mPic;
                                    final TextView mAge;
                                    final TextView mName;


                                    final String c_User_Id;
                                    //try {
                                        c_User_Id = snapshot.child("User").getValue(String.class);
                                        DatabaseReference getName = db.getInstance().getReference().child("Users").child(c_User_Id).child("first name");
                                        DatabaseReference getAge = db.getInstance().getReference().child("Users").child(c_User_Id).child("age");

                                        //for(int i = 0; i < attendNum; i++) {
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

                                        //if(fName == ""){
                                          //  mStorageRef.child("UserPictures").child(c_User_Id + "pic1.jpg").delete();
                                          //  db.getReference().child("Event").child(snapshot.toString()).removeValue();
                                        //}else {
                                            mStorageRef.child("UserPictures").child(c_User_Id + "pic1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                                                                Intent intent = new Intent(EventActivity.this, ViewOtherActivity.class);
                                                                intent.putExtra("USER_ID", c_User_Id);
                                                                startActivity(intent);

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
                                                    Toast.makeText(EventActivity.this, snapshot.getKey(), Toast.LENGTH_SHORT).show();
                                                    FirebaseDatabase.getInstance().getReference().child("Event").child(""+Event_ID).child("Attending").child(snapshot.getKey()).removeValue();
                                                    try {
                                                        mStorageRef.child("UserPictures").child(c_User_Id + "pic1.jpg").delete();
                                                    }catch (Exception e){

                                                    }
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
