package com.example.firebasedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.currentTimeMillis;

public class shopActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ListView mListview;
    private ArrayList<Book> mArrData;
    private ShopAdapter mAdapter;
    private DatabaseReference db;

    private Spinner spinner;
// ...

    private FirebaseAuth mAuth;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        mAuth = FirebaseAuth.getInstance();
        mListview = (ListView) findViewById(R.id.list_shop);
        db = FirebaseDatabase.getInstance().getReference("books");
        // Set some data to array list


        //spinner
        spinner = (Spinner) findViewById(R.id.shopSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinner_data_shop,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null) {
            Log.d("frin shop", "onStart: "+currentUser.getUid());
            userID = currentUser.getUid();
           BookExchangeApp app=(BookExchangeApp) getApplication();
           app.startServiceFromApp(userID.toString());
            retrieveBooks();
        }
        else{
            startActivity(new Intent(
                    getApplicationContext(),
                    LoginActivity.class));
            finish();
        }
    }


    protected void retrieveBooks() {

db.addValueEventListener(new ValueEventListener(){
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // This method is called once with the initial value and again
        // whenever data at this location is updated.
        ArrayList<Book> test = new ArrayList<Book>();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            test.add(ds.getValue(Book.class));
        }
        updateDisplay(test);
    }
    @Override
    public void onCancelled(DatabaseError error) {
        // Failed to read value
        Log.w("Newcode", "Failed to read value.", error.toException());
    }

});
    }

    protected void retrieveBooksByGenre(String genre) {

        db.orderByChild("genre")
                .equalTo(genre).addListenerForSingleValueEvent(new ValueEventListener(){
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(
                R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_selling:
                startActivity(new Intent(
                        getApplicationContext(),
                        sellActivity.class));

                return true;
            case R.id.menu_buying://empty beacuse we are in the activity already.
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

    private void updateDisplay(ArrayList<Book> t) {


        // Initialize adapter and set adapter to list view



                mArrData = t;
                Log.d("updateDisplay", "updateDisplay: "+userID);
                mAdapter = new ShopAdapter(this, mArrData,userID);
                mListview.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0){
            String genre;

            switch (position){
                case 1:
                    genre = "Fantasy";
                    break;
                case 2:
                    genre = "Science fiction";
                    break;
                case 3:
                    genre = "Romance";
                    break;
                case 4:
                    genre = "Thriller";
                    break;
                case 5:
                    genre = "Mystery";
                    break;
                case 6:
                    genre = "Horror";
                    break;
                default:
                    genre="Fantasy";
            }
            Log.d("TAG", "onItemSelected: "+genre);
            retrieveBooksByGenre(genre);

        }
        else
            retrieveBooks();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    //nothing here
    }
}
