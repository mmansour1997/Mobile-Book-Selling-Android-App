package com.example.firebasedb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;

public class soldItemsAdapter extends BaseAdapter {
    Context context;
    ArrayList<Book> soldBookList;

    public soldItemsAdapter(Context context, ArrayList<Book> soldBookList) {
        super();
        this.context = context;
        this.soldBookList = soldBookList;
    }

    @Override
    public int getCount() {
        return soldBookList.size();
    }

    @Override
    public Object getItem(int position) {
        return soldBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return soldBookList.get(position).uniqueID;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //we are doing this because its outside the activity
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.solditem_list, parent, false);

        TextView bookName = (TextView)convertView.findViewById(R.id.soldItemName);
        TextView bookPrice = (TextView)convertView.findViewById(R.id.soldPrice);
        Button editBtn = (Button) convertView.findViewById(R.id.editButton);
        Button deleteBtn = (Button) convertView.findViewById(R.id.deleteButton);

        // Set the title and button name
        bookName.setText(soldBookList.get(position).bookName);
        bookPrice.setText(soldBookList.get(position).price);


        // Click listener of button
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //request here
                Log.d("Clicked On",Long.toString(soldBookList.get(position).uniqueID));

               Intent intent = new Intent(context, addItemActivity.class);

                intent.putExtra("row_id",soldBookList.get(position).uniqueID);
                intent.putExtra("name",soldBookList.get(position).bookName);
                intent.putExtra("ISBN",soldBookList.get(position).ISBN);
                intent.putExtra("price",soldBookList.get(position).price);
                intent.putExtra("genre",soldBookList.get(position).genre);
                context.startActivity(intent);

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //request here
                Log.d("Clicked On",Long.toString(soldBookList.get(position).uniqueID));



                ConnectivityManager connectivityManager = (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo =
                        connectivityManager.getActiveNetworkInfo();

// if network is connected, download feed

                if (networkInfo != null && networkInfo.isConnected()) {

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(context);

                    // set dialog title & message, and provide Button to dismiss
                    builder.setTitle("Confirm");
                    builder.setMessage("Deleting this item will result in deleting all requests that are linked to this item");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.deleteBook(soldBookList.get(position).uniqueID,context);
                            soldBookList.remove(position);
                            notifyDataSetChanged(); //update
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show(); // display the Dialog
                }

                else
                    Toast.makeText(context, "You need an internet connection for this action", Toast.LENGTH_LONG).show();



            }
        });

        return convertView;
    }
}
