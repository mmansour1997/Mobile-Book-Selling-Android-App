package com.example.firebasedb;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class BookExchangeApp extends Application {
    Intent service;
    private long requests = 0;
    private String userID=null;
    public void setRequests(long requests) {
        this.requests = requests;
    }

    public long getRequests() {
        return requests;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Book Exchange", "App started");
    }
public void startServiceFromApp(String ID){
        this.userID = ID;
    service = new Intent(getApplicationContext(), BookExchangeService.class);
    this.startService(service);
}
public void stopSerive(){
        this.stopService(service);
}
}
