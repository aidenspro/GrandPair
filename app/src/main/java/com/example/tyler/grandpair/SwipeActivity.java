package com.example.tyler.grandpair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
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
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SwipeActivity extends Activity {

    private cards cards_data[];
    //private ArrayList<String> al;
    private arrayAdapter arrayAdapter;
    private int i;
    private int j = 0;
    private StorageReference mStorageRef;
    private DatabaseReference event_db;
    private FirebaseAuth mAuth;
    private ImageView mImageView;
    private int attendNum;
    CountDownLatch done;
    String URL;
    ListView listView;
    List<cards> rowItems;
    @BindView(R.id.frame) SwipeFlingAdapterView flingContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        event_db = db.getInstance().getReference().child("Event");
        DatabaseReference addAttendee = db.getInstance().getReference().child("Event").child(""+j).child("Attending").push();
        i = 0;


        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final String User_id = mAuth.getCurrentUser().getUid();

        ButterKnife.bind(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();


        //al = new ArrayList<>();

        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );

        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);



        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                makeToast(SwipeActivity.this, "Left!");
                j++;
            }

            @Override
            public void onRightCardExit(Object dataObject) {
            int fAttend = 0;

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference Attend = db.getInstance().getReference().child("Event").child("" + j).child("AttendNum");

                Attend.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        attendNum = dataSnapshot.getValue(Integer.class);
                        makeToast(SwipeActivity.this, "num " + attendNum);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        makeToast(SwipeActivity.this, "fail");
                    }
                });

                try {


                    DatabaseReference addAttendee = db.getInstance().getReference().child("Event").child("" + j).child("Attending");

                    fAttend = attendNum + 1;
                    Attend.setValue(fAttend);

                    Map newPost = new HashMap();
                    newPost.put("User", User_id);
                    addAttendee.setValue(newPost);

                    makeToast(SwipeActivity.this, "Right!");

                } catch (Exception e){
                    makeToast(SwipeActivity.this, "fail");
            }
                Intent intent = new Intent(SwipeActivity.this, EventActivity.class);
                //intent.putExtra(URL,i);

                intent.putExtra("EVENT_ID",j);
                intent.putExtra("ATTEND_NUM",fAttend);
                startActivity(intent);
                //finish();

                j++;

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                final RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.mipmap.ic_launcher_round);

                mStorageRef.child("EventPictures").child(i+"pic1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override

                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        //Drawable drawable = LoadImageFromWebOperations(uri.toString());
                        //try {
                            URL = (uri.toString());
                            //Glide.with(getApplicationContext()).load(uri).apply(options).into(mImageView);
                            //mImageView =(ImageView)findViewById(R.id.image);
                           // mImageView.setImageResource(R.drawable.login);
                            cards Item = new cards(User_id,"Event",uri.toString());
                            rowItems.add(Item);
                            arrayAdapter.notifyDataSetChanged();
                            Log.d("LIST", "notified");
                            i++;

                           // Toast.makeText(SwipeActivity.this, uri.toString(), Toast.LENGTH_SHORT).show();
                            //mImageView.setImageDrawable(drawable);
                        //} catch (MalformedURLException e) {

                       //}


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });



                //hello = mImageView;
                //al.add("Swipe ".concat(String.valueOf(i)));

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(SwipeActivity.this, "Clicked!");
                Intent intent = new Intent(SwipeActivity.this, EventActivity.class);
                //intent.putExtra(URL,i);
                intent.putExtra("CURRENT_URL",URL);
                intent.putExtra("EVENT_ID",j);
                intent.putExtra("ATTEND_NUM",attendNum);
                startActivity(intent);
                //finish();
                return;

            }
        });
    }
    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }





}