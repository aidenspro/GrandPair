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
import java.util.List;
import java.util.concurrent.CountDownLatch;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SwipeActivity extends Activity {

    private cards cards_data[];
    //private ArrayList<String> al;
    private arrayAdapter arrayAdapter;
    private int i = 0;
    private int j = 0;
    private StorageReference mStorageRef;
    private DatabaseReference event_db;
    private FirebaseAuth mAuth;
    private ImageView mImageView;
    private boolean swiped;
    private int attendNum;
    private int occurence = 0;
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
        DatabaseReference addAttendee = db.getInstance().getReference().child("Event").child(""+j).child("Attending");



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
                //makeToast(SwipeActivity.this, "Left!");

                j++;
            }

            @Override
            public void onRightCardExit(final Object dataObject) {

                new Thread(new Runnable() {
                    public void run() {
                event_db.child(""+j).child("AttendNum").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        attendNum = dataSnapshot.getValue(Integer.class);
                        if(swiped == false) {
                            Intent intent = new Intent(SwipeActivity.this, EventActivity.class);
                            intent.putExtra("ATTEND_NUM", attendNum);
                            intent.putExtra("EVENT_ID", j);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            swiped = true;
                            startActivity(intent);

                        }
                        j++;


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
                    }
                }).start();
                //intent.putExtra(URL,i);
                //finish();
                swiped = false;

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if(itemsInAdapter < 1)
                new Thread(new Runnable() {
                    public void run() {

                        mStorageRef.child("EventPictures").child(i + "pic1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                cards Item = new cards(User_id, "Event", uri.toString());
                                rowItems.add(Item);
                                arrayAdapter.notifyDataSetChanged();
                                Log.d("LIST", "notified");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                        i++;
                    }
                }).start();


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
                //makeToast(SwipeActivity.this, "Clicked!");
                Intent intent = new Intent(SwipeActivity.this, EventActivity.class);
                //intent.putExtra(URL,i);
                intent.putExtra("HOW",1);
                intent.putExtra("EVENT_ID",j);
                intent.putExtra("ATTEND_NUM",attendNum);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
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