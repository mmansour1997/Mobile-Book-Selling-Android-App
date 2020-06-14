package com.example.firebasedb;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookExchangeService extends Service {
    String userID;
   BookExchangeApp app;
    public BookExchangeService() {
    }
    @Override
    public void onCreate() {
        app=(BookExchangeApp) getApplication();
        this.userID = app.getUserID();
        Log.d("APP", "Service Started ");
        retrieveSellingRequests();
    }

    @Override
    public int onStartCommand(Intent intent, int flags,
                              int startId) {
        // Code that's executed each time another component
        // starts the service by calling the startService method.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        // Code that's executed once when the service
        // is no longer in use and is being destroyed.
    }
    protected void retrieveSellingRequests() {

        DatabaseReference db;
        db = FirebaseDatabase.getInstance().getReference("requests");
        db.orderByChild("sellerID")
                .equalTo(userID).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getChildrenCount()>app.getRequests()){

                    sendNotification();
                    //sendnoti();
                }
                app.setRequests(dataSnapshot.getChildrenCount());


            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Newcode", "Failed to read value.", error.toException());
            }

        });
    }
    private void sendNotification()
    {

        // create the intent for the notification
        Intent notificationIntent = new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        // create the pending intent
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, flags);

        // create the variables for the notification
        int icon = R.drawable.ic_launcher_background;
        CharSequence tickerText = "News update available!";
        CharSequence contentTitle = "Someone wants you buy your book!";
        CharSequence contentText = "Click here to go to requests";

        NotificationChannel notificationChannel = new NotificationChannel("Channel_ID", "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);


        // create the notification and set its data
        Notification notification =
                new Notification.Builder(this, "Channel_ID")
                        .setSmallIcon(icon)
                        .setTicker(tickerText)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setChannelId("Channel_ID")
                        .build();

        final int NOTIFICATION_ID = 1;
        manager.notify(NOTIFICATION_ID, notification);
    }

}
