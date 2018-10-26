package com.example.tyler.grandpair;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {
    private int PICK_IMAGE_REQUEST;
    private FirebaseAuth mAuth;
    private Uri mImageUri;
    private ImageView mImageView;
    private StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        String User_id = mAuth.getCurrentUser().getUid();
        mImageView =(ImageView)findViewById(R.id.profilePicture);
        String start = "https://firebasestorage.googleapis.com/v0/b/elderly-social-network.appspot.com/o/";
        String end = "pic1.jpg?alt=media&token=aabc5f4c-bc6b-4226-b774-82f9bf604dbc";

        mStorageRef.child(User_id + "pic1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Drawable drawable = LoadImageFromWebOperations(uri.toString());
                try {
                    URL url = new URL(uri.toString());
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    mImageView.setImageBitmap(bmp);
                    //Toast.makeText(ProfileActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                    mImageView.setImageDrawable(drawable);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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
    private Drawable LoadImageFromWebOperations(String url) {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
           // mImageView.setImageDrawable(d);
            return d;
        }catch (Exception e) {
            System.out.println("Exc="+e);
            return null;
        }
    }



}


