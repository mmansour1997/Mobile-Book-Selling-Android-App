package com.example.firebasedb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ShopAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Book> bookData;
    String userID;
    public ShopAdapter(Context context , ArrayList<Book> mArrData,String userID) {
        super();
        mContext = context;
        bookData = mArrData;
        this.userID = userID;
    }

    @Override
    public int getCount() {
        return bookData.size();
    }

    @Override
    public Object getItem(int position) {
        return bookData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookData.get(position).uniqueID;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
            //we are doing this because its outside the activity
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.shop_item, parent, false);

        TextView bookName = (TextView)convertView.findViewById(R.id.bookName);
        TextView bookPrice = (TextView)convertView.findViewById(R.id.priceTextView);
        TextView bookISBN=(TextView) convertView.findViewById(R.id.ISBN);
        Button reqBtn = (Button) convertView.findViewById(R.id.requestButton);

        // Set the title and button name
        bookName.setText(bookData.get(position).bookName);
        bookPrice.setText(bookData.get(position).price);
        Log.d("ISBN", "getView: "+bookData.get(position).getISBN());
        bookISBN.setText(bookData.get(position).getISBN());

        // Click listener of button
        reqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //request here
//                Log.d("Clicked On",bookData.get(position).sellerID); //create Request here
                ConnectivityManager connectivityManager = (ConnectivityManager)
                        mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo =
                        connectivityManager.getActiveNetworkInfo();

// if network is connected, download feed

                if (networkInfo != null && networkInfo.isConnected()) {

                        if(!bookData.get(position).sellerID.equals(ShopAdapter.this.userID)) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            //make sure the user is not requesting their own book
                            Request req = new Request(userID, bookData.get(position).sellerID, Long.toString(bookData.get(position).uniqueID)+userID, Long.toString(bookData.get(position).uniqueID),bookData.get(position).getBookName(),dtf.format(now));
                            MainActivity.createRequests(req,mContext);
                        }
                        else
                            Toast.makeText(mContext, "You cannot request your own item!", Toast.LENGTH_LONG).show();
                    }
                else
                    Toast.makeText(mContext, "You need an internet connection for this action", Toast.LENGTH_LONG).show();
                }});

        return convertView;

                                  }
// if network is connected, download feed




}
