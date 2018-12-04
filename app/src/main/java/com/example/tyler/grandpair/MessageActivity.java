package com.example.tyler.grandpair;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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
    private int messageNum2;
    private String User_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        sendMessage = (Button) findViewById((R.id.send));
        mScroll = (ScrollView) findViewById(R.id.scroll);
        mScroll2 = (ScrollView) findViewById(R.id.scroll2);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        messageNum = getIntent().getIntExtra("MESSAGE_NUM", 0);
        messageNum2 = getIntent().getIntExtra("MESSAGE_NUM", 0);
        how = getIntent().getIntExtra("HOW", 0);
        USER_ID = getIntent().getStringExtra("USER_ID");
        messageBox =(EditText) findViewById(R.id.messageBox);
        mAuth = FirebaseAuth.getInstance();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        User_id = mAuth.getCurrentUser().getUid();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = messageBox.getText().toString();



                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference current_user_db = db.getInstance().getReference().child("Users").child(USER_ID).child("Messages").child(User_id).child("Messages");

                Map newPost = new HashMap();
                newPost.put("message", message);
                newPost.put("Type", "Recieved");
                current_user_db.child(""+ messageNum).setValue(newPost);
                messageNum++;
                current_user_db.child("MessageNum").setValue(messageNum);



                db = FirebaseDatabase.getInstance();
                DatabaseReference other_user_db = db.getInstance().getReference().child("Users").child(User_id).child("Messages").child(USER_ID).child("Messages");

                newPost = new HashMap();
                newPost.put("message", message);
                newPost.put("Type", "Sent");
                other_user_db.child(""+ messageNum2).setValue(newPost);
                messageNum2++;
                other_user_db.child("MessageNum").setValue(messageNum2);

            }


                });




        //------------------------------------------------------------------Generate mini account
        mImageView = (ImageView) findViewById(R.id.eventPicture);

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


        FirebaseDatabase.getInstance().getReference().child("Users").child(User_id).child("Messages").child(USER_ID).child("Messages")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final LinearLayout ll = new LinearLayout(MessageActivity.this);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        ll.setY(100);


                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            final TextView mMessage;
                            TextView m1;
                            final View f = inflater.inflate(R.layout.messagemini, null);
                            final View g = inflater.inflate(R.layout.messageminiright, null);

                            try {
                                if (snapshot.child("Type").getValue(String.class).equals("Sent")) {
                                    m1 = (TextView) f.findViewById(R.id.message);
                                    m1.setBackgroundColor(Color.GREEN);
                                } else {
                                    m1 = (TextView) g.findViewById(R.id.message);
                                    m1.setBackgroundColor(Color.BLUE);
                                }
                            }catch(Exception e){
                                m1 = (TextView) f.findViewById(R.id.message);
                            }

                            mMessage = m1;


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

                            try {
                                if (snapshot.child("Type").getValue(String.class).equals("Sent")) {
                                    m1.setBackgroundColor(Color.GREEN);
                                    ll.addView(f);
                                } else {
                                    m1.setBackgroundColor(Color.BLUE);
                                    ll.addView(g);
                                }
                            }catch(Exception e){
                            }


                        }
                        mScroll2.addView(ll);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });







    }
}

