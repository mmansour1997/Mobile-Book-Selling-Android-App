package com.example.firebasedb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class sellActivity extends
        AppCompatActivity {
DatabaseReference db;
    ListView mListview;
    private ArrayList<Book> mArrData;
    private String userID;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        db = FirebaseDatabase.getInstance().getReference("books");

        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(sellActivity.this, addItemActivity.class);
               startActivity(intent);
            }
        });
         mListview = (ListView) findViewById(R.id.soldItemList);

        // Initialize adapter and set adapter to list view

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null) {
            Log.d("frin shop", "onStart: "+currentUser.getUid());
            userID=currentUser.getUid();
            retrieveUserItems();
        }
        else{
            startActivity(new Intent(
                    getApplicationContext(),
                    LoginActivity.class));
            finish();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(
                R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_selling://empty beacause we are already in the activity.
                return true;
            case R.id.menu_buying:
                startActivity(new Intent(
                        getApplicationContext(),
                        shopActivity.class));

                return true;
            case R.id.menu_requests:
                startActivity(new Intent(
                        getApplicationContext(),
                        MainActivity.class));

                return true;
            case R.id.logoutbtn:
                mAuth.signOut();
                BookExchangeApp app = (BookExchangeApp) getApplication();
                app.stopSerive();
                startActivity(new Intent(
                        getApplicationContext(),
                        LoginActivity.class));
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    protected void retrieveUserItems() {
        Log.d("retrieving", "retrieveBuyingRequests: "+userID);
                db.orderByChild("sellerID")
                .equalTo(userID).addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        ArrayList<Book> test = new ArrayList<Book>();
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            test.add(ds.getValue(Book.class));
                        }
                        Log.d("TAG", "onDataChange: "+test.size());
                        updateDisplay(test);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("Newcode", "Failed to read value.", error.toException());
                    }

                });


    }

    private void updateDisplay(ArrayList<Book> t) {


        // Initialize adapter and set adapter to list view


        if (t.size() != 0) {
            mArrData = t;

            soldItemsAdapter mAdapter = new soldItemsAdapter(this, mArrData);
            mListview.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();


        }
    }

}
