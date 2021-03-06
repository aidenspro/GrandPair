package com.example.tyler.grandpair;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class SettingsActivity extends AppCompatActivity {

    private Button mEventSettings;
    private Button mProfileSettings;
    private Button mCreateEvent;
    private Button mChangeEmail;
    private Button mChangePassword;
    private Button mRemoveAccount;
    private Button mRemoveEvent;
    private Button mAdmin;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mProfileSettings = (Button) findViewById(R.id.profileSettings);
        mEventSettings = (Button) findViewById(R.id.createEvent);
        mCreateEvent = (Button) findViewById(R.id.createEvent);
        mChangeEmail = (Button) findViewById(R.id.changeEmail);
        mChangePassword = (Button) findViewById(R.id.changePassword);
        mRemoveAccount = (Button) findViewById(R.id.removeAccount);
        mRemoveEvent= (Button) findViewById(R.id.removeEvent);
        mAdmin= (Button) findViewById(R.id.Admin);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        DatabaseReference ref = db.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Admin");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue(String.class).equals("True"))
                        mAdmin.setVisibility(View.VISIBLE);
                }catch(Exception e){

                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });

        mProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, CreateProfile.class);
                startActivity(intent);

                return;

            }
        });
        mEventSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, CreateEventActivity.class);
                startActivity(intent);

                return;

            }
        });
        mChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "Update Email Sent!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return;

            }
        });
        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = auth.getCurrentUser().getEmail();
                auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "Reset Email Sent!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return;

            }
        });
        mRemoveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            mStorageRef.child("UserPictures").child(user.getUid() + "pic1.jpg").delete();
                            db.getReference().child("users").child(user.getUid()).removeValue();
                        }
                    }
                });

                return;

            }
        });

        mRemoveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                return;

            }
        });

        mAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AdminActivity.class);
                startActivity(intent);

                return;

            }
        });

    }
        public void forgotPassword() {

        }

        public void changeEmail() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updateEmail("user@example.com").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    }
                }
            });
        }

        public void changePassword() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String newPassword = "SOME-SECURE-PASSWORD";
            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    }
                }
            });
        }
        public void deleteUser() {

        }
    }





