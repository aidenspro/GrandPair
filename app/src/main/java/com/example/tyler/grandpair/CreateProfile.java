package com.example.tyler.grandpair;

import android.app.Activity;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {

    private Button mProfile,mSelect;
    private EditText mFirstName, mLastName, mAge;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener fbListener;
    private int PICK_IMAGE;
    private String USER_ID;
    private StorageReference mStorageRef;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_reg);
        mAuth = FirebaseAuth.getInstance();
        mProfile = (Button) findViewById(R.id.saveProfile);
        mSelect = (Button) findViewById(R.id.selectImage);
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
        mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = mFirstName.getText().toString();
                final String lastName = mLastName.getText().toString();
                final String age = mAge.getText().toString();

                String User_id = mAuth.getCurrentUser().getUid();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference current_user_db = db.getInstance().getReference().child("Users").child(User_id);
                USER_ID = User_id;

                pickImage();

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
        StorageReference mountainsRef = mStorageRef.child(USER_ID + "pic1.jpg");
        StorageReference mountainImagesRef = mStorageRef.child("images/mountains.jpg");
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
                    Toast.makeText(CreateProfile.this, "Picture Uploaded", Toast.LENGTH_SHORT).show();
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });

        }

    }

}
