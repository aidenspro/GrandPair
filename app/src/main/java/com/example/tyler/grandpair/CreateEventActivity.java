package com.example.tyler.grandpair;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {

    private Button mProfile,mSelect;
    private EditText mFirstName, mLastName, mAge;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fbListener;
    private int PICK_IMAGE;
    private Uri url;
    private int Event_ID;
    private StorageReference mStorageRef;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_event);
        mAuth = FirebaseAuth.getInstance();
        mProfile = (Button) findViewById(R.id.saveProfile);
        mSelect = (Button) findViewById(R.id.selectImage);
        mFirstName = (EditText) findViewById(R.id.firstName);
        mLastName = (EditText) findViewById(R.id.lastName);
        mAge = (EditText) findViewById(R.id.age);
        //Event_ID = mFirstName.getText().toString();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference event_db = db.getInstance().getReference().child("Event").child("NumEvents");

        final String firstName = mFirstName.getText().toString();

        event_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event_ID = dataSnapshot.getValue(Integer.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        mProfile.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                                            DatabaseReference event_db = db.getInstance().getReference().child("Event");
                                            int eventNum;
                                            eventNum = Event_ID;
                                            final String firstName = mFirstName.getText().toString();
                                            final String lastName = mLastName.getText().toString();
                                            final String age = mAge.getText().toString();
                                            Event_ID = eventNum;

                                            DatabaseReference current_user_db = db.getInstance().getReference().child("Event").child(""+Event_ID);


                                            //Event_ID = firstName;

                                            Map newPost = new HashMap();
                                            newPost.put("Name", firstName);
                                            newPost.put("Location", lastName);
                                            newPost.put("Date", age);
                                            newPost.put("AttendNum", 0);

                                            current_user_db.setValue(newPost);

                                            Intent intent = new Intent(CreateEventActivity.this, ChoicesActivity.class);
                                            startActivity(intent);
                                            eventNum++;
                                            event_db.child("NumEvents").setValue(eventNum);


                                            finish();

                                        }
                                    }

        );
        mSelect.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           final String firstName = mFirstName.getText().toString();
                                           final String lastName = mLastName.getText().toString();
                                           final String age = mAge.getText().toString();
                                           final String URL = mAge.getText().toString();

                                           String User_id = mAuth.getCurrentUser().getUid();
                                           FirebaseDatabase db = FirebaseDatabase.getInstance();
                                           DatabaseReference current_user_db = db.getInstance().getReference().child("Event").child(""+Event_ID).child("URL");
                                           pickImage();
                                           current_user_db.setValue(url);
                                           //USER_ID = User_id;



                                       }
                                   }

        );
    }


    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference mountainsRef = mStorageRef.child("EventPictures").child(Event_ID + "pic1.jpg");

        InputStream IS = null;

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                IS = this.getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            UploadTask uploadTask = mountainsRef.putStream(IS);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CreateEventActivity.this, "Picture Uploaded", Toast.LENGTH_SHORT).show();

                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }

            });

        }

    }

}