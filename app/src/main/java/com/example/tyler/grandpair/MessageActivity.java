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
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    private String fMessage;
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
    private ScrollView mScroll2;
    private StorageReference mStorageRef;
    private String USER_ID;
    private Button sendMessage;
    private EditText messageBox;
    private int messageNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        sendMessage = (Button) findViewById((R.id.send));
        mScroll = (ScrollView) findViewById(R.id.scroll);
        mScroll2 = (ScrollView) findViewById(R.id.scroll2);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        attendNum = getIntent().getIntExtra("ATTEND_NUM", 0);
        how = getIntent().getIntExtra("HOW", 0);
        USER_ID = getIntent().getStringExtra("USER_ID");
        messageBox =(EditText) findViewById(R.id.messageBox);
        mAuth = FirebaseAuth.getInstance();
        messageNum = 0;

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = messageBox.getText().toString();

                String User_id = mAuth.getCurrentUser().getUid();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference current_user_db = db.getInstance().getReference().child("Users").child(User_id).child("Messages").child(USER_ID).child("Messages");

                    current_user_db.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                messageNum = dataSnapshot.child("MessageNum").getValue(Integer.class);
                            }catch (Exception e){
                                messageNum = -1;
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            messageNum = 0;
                        }
                    });

                    if(messageNum == -1){
                        current_user_db.child("MessageNum").setValue(0);}


                Map newPost = new HashMap();
                newPost.put("message", message);

                current_user_db.child(""+ messageNum).setValue(newPost);
                messageNum++;
                current_user_db.child("MessageNum").setValue(messageNum);
            }
                });




        //------------------------------------------------------------------Generate mini account
        mImageView = (ImageView) findViewById(R.id.eventPicture);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().getReference().child("Users").child(USER_ID)
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
                                    final TextView mMessage;


                                    final String c_User_Id;

                                    c_User_Id = USER_ID;
                                    DatabaseReference getName = db.getInstance().getReference().child("Users").child(c_User_Id).child("first name");
                                    DatabaseReference getAge = db.getInstance().getReference().child("Users").child(c_User_Id).child("age");


                                    final View v = inflater.inflate(R.layout.profilemini, null);

                                    mMessage = (TextView) v.findViewById(R.id.nameMini);
                                    mAge = (TextView) v.findViewById(R.id.ageNum);
                                    mPic = (ImageView) v.findViewById(R.id.miniPic);


                                    getName.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            fMessage = dataSnapshot.getValue(String.class);
                                            mMessage.setText(fMessage);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            fMessage = "Removed";
                                            mMessage.setText(fMessage);

                                        }
                                    });
                                    getAge.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            fMessage = dataSnapshot.getValue(String.class);
                                            mAge.setText(fMessage);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            fMessage = "User";
                                            mAge.setText(fMessage);
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


        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Messages").child(USER_ID).child("Messages")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        final FirebaseDatabase db = FirebaseDatabase.getInstance();
                        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        //View v = inflater.inflate(R.layout.profilemini, null);
                        final LinearLayout ll = new LinearLayout(MessageActivity.this);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            new Thread(new Runnable() {
                                public void run() {

                                    final TextView mMessage;

                                    final View v = inflater.inflate(R.layout.messagemini, null);

                                    mMessage = (TextView) v.findViewById(R.id.message);


                                    Toast.makeText(MessageActivity.this, "here", Toast.LENGTH_SHORT).show();
                                    snapshot.getRef().child("message").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            fMessage = snapshot.child("message").getValue(String.class);
                                            mMessage.setText(fMessage);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            fMessage = "Removed";
                                            mMessage.setText(fMessage);

                                        }
                                    });


                                }
                            }).start();

                        }
                        mScroll2.addView(ll);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });







    }
}

