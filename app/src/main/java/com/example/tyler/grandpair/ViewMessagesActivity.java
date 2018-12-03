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

public class ViewMessagesActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView mEventName;
    private ScrollView mScroll;
    private ImageView mImageView;
    private String fName;
    private String lName;
    private String url;
    private String age;
    private int Event_ID;
    private int how;
    private int messageNum;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);
        mScroll = (ScrollView) findViewById(R.id.scroll);
        mEventName = (TextView) findViewById(R.id.location);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        final String User_id = mAuth.getCurrentUser().getUid();



        FirebaseDatabase.getInstance().getReference().child("Users").child(User_id).child("Messages")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        final FirebaseDatabase db = FirebaseDatabase.getInstance();
                        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        //View v = inflater.inflate(R.layout.profilemini, null);
                        final LinearLayout ll = new LinearLayout(ViewMessagesActivity.this);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            new Thread(new Runnable() {
                                public void run() {

                                    final ImageView mPic;
                                    final TextView mAge;
                                    final TextView mName;


                                    final String c_User_Id;
                                    //try {
                                    c_User_Id = snapshot.getKey();
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

                                                DatabaseReference current_user_db = db.getInstance().getReference().child("Users").child(c_User_Id).child("Messages").child(User_id).child("Messages");
                                                current_user_db.addValueEventListener(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                            messageNum = dataSnapshot.child("MessageNum").getValue(Integer.class);
                                                        }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        messageNum = 0;
                                                    }
                                                });
                                                URL url = new URL(uri.toString());
                                                Glide.with(getApplicationContext()).load(uri).into(mPic);
                                                mPic.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(ViewMessagesActivity.this, MessageActivity.class);
                                                        intent.putExtra("USER_ID", c_User_Id);
                                                        intent.putExtra("MESSAGE_NUM",messageNum);
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
                                            Toast.makeText(ViewMessagesActivity.this, snapshot.getKey(), Toast.LENGTH_SHORT).show();
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




    }
}
