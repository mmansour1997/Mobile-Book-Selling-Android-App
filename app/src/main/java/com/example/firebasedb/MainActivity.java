package com.example.firebasedb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public ListView itemsListView;
    public ListView sellingListView;

static DatabaseReference db;
    private FirebaseAuth mAuth;
    ArrayList<Request> buyingrequests;
    ArrayList<Request> sellingrequests;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseDatabase.getInstance().getReference("requests");
        itemsListView = (ListView) findViewById(R.id.ItemView);
        sellingListView = (ListView) findViewById(R.id.listViewSeller);
        itemsListView.setOnItemClickListener(this);
        sellingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent viewContact =
                        new Intent(getApplicationContext(), ChatRoom.class);

                // pass the selected contact's row ID as an extra with the Intent
                viewContact.putExtra("userName", "Seller");
                viewContact.putExtra("roomName", sellingrequests.get(position).getChatRoomID());
                Log.d("sent", "onItemClick: "+sellingrequests.get(position).getChatRoomID());
                startActivity(viewContact); // start the ViewContact Activity
            }
        });
        mAuth = FirebaseAuth.getInstance();
//        Map<String, Object> user = new HashMap<>();
//        user.put("seller", SellerID);
//        user.put("buyer", BuyerID);
//        user.put("itemID", ItemID);
//        user.put("chatID", chatID);




        // create a List of Map<String, ?> objects


    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null) {
            Log.d("frin shop", "onStart Called: "+currentUser.getUid());
            userID=currentUser.getUid();

            retrieveBuyingRequests(); //myID
            retrieveSellingRequests();
        }
        else{
            startActivity(new Intent(
                    getApplicationContext(),
                    LoginActivity.class));
            finish();
        }
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    public void updateSelling( ArrayList<Request>t)
    {
        sellingrequests = t;
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        for (Request item : t) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Name", item.getBookName());
            map.put("Date", item.getRequestDate());
            data.add(map);
        }
        if (t.size() != 0) {


            // create the resource, from, and to variables
            int resource = R.layout.requestsell;
            String[] from = {"Name","Date"};
            int[] to = {R.id.bookSellingNameTextView,R.id.reqSellingDataTextView};

            // create and set the adapter
            SimpleAdapter adapter = //siminlar to class.
                    new SimpleAdapter(this, data, resource, from, to);

            sellingListView.setAdapter(adapter);

            setListViewHeightBasedOnChildren(sellingListView);

        }

    }
    public void updateBuying( ArrayList<Request>t)
    {
        buyingrequests = t;
        if (t.size() != 0) {
            ArrayList<HashMap<String, String>> data =
                    new ArrayList<HashMap<String, String>>();
            for (Request item : t) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Name", item.getBookName());
                map.put("Date", item.getRequestDate());
                data.add(map);
            }

            // create the resource, from, and to variables
            int resource = R.layout.itemview;
            String[] from = {"Name","Date"};
            int[] to = {R.id.bookBuyingName,R.id.reqBuyingDataTextView};

            // create and set the adapter
            SimpleAdapter adapter = //siminlar to class.
                    new SimpleAdapter(this, data, resource, from, to);

            itemsListView.setAdapter(adapter);

            setListViewHeightBasedOnChildren(itemsListView);

        }
    }
    protected void retrieveBuyingRequests() {

        db.orderByChild("buyerID")
                .equalTo(userID).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<Request> test = new ArrayList<Request>();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    test.add(ds.getValue(Request.class));
                }
                Log.d("TAG", "onDataChange: "+test.size());
                updateBuying(test);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Newcode", "Failed to read value.", error.toException());
            }

        });
    }
    protected void retrieveSellingRequests() {
        db.orderByChild("sellerID")
                .equalTo(userID).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<Request> test = new ArrayList<Request>();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    test.add(ds.getValue(Request.class));
                }
                Log.d("TAG", "onDataChange: "+test.size());
                updateSelling(test);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Newcode", "Failed to read value.", error.toException());
            }

        });
    }
    public static void createRequests(final Request req,final Context context) {

        db = FirebaseDatabase.getInstance().getReference("requests");
        db.orderByChild("chatRoomID")
                .equalTo(req.getChatRoomID()).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("test", "onDataChange: "+dataSnapshot.getChildrenCount());
                if(dataSnapshot.getChildrenCount() == 0){
                    db.child(req.getChatRoomID()).setValue(req);//added
                }
                else
                {

                    Toast.makeText(context, "You already requested this item!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Newcode", "Failed to read value.", error.toException());
            }

        });

    }
    public static void deleteBook(final long bookID,final Context context) {
       final DatabaseReference bookDB=FirebaseDatabase.getInstance().getReference("books");
       final DatabaseReference reqDB=FirebaseDatabase.getInstance().getReference("requests");
        bookDB.orderByChild("uniqueID")
                .equalTo(bookID).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                    dataSnapshot.getRef().removeValue();
                    //now delete all requests for this item
                    reqDB.orderByChild("itemID")
                            .equalTo(Long.toString(bookID)).addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                                Log.d("about to tdlete", "onDataChange: "+Snapshot.getKey());
                                Snapshot.getRef().removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("Newcode", "Failed to read value.", error.toException());
                        }

                    });

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
            case R.id.menu_buying:
                startActivity(new Intent(
                        getApplicationContext(),
                        shopActivity.class));

                return true;
            case R.id.menu_requests://empty because we are already in the activity.
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
               getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo =
                connectivityManager.getActiveNetworkInfo();

// if network is connected, download feed

        if (networkInfo != null && networkInfo.isConnected()) {

            Intent viewContact =
                    new Intent(this, ChatRoom.class);

            // pass the selected contact's row ID as an extra with the Intent
            viewContact.putExtra("userName", "Buyer");
            viewContact.putExtra("roomName", buyingrequests.get(position).getChatRoomID());
            Log.d("sent", "onItemClick: "+buyingrequests.get(position).getChatRoomID());
            startActivity(viewContact); // start the ViewContact Activity
            }

        else
            Toast.makeText(getApplicationContext(), "You need an internet connection for this action", Toast.LENGTH_LONG).show();

    }
}

