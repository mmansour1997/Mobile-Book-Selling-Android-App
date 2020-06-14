package com.example.firebasedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static java.lang.System.currentTimeMillis;

public class addItemActivity extends AppCompatActivity implements View.OnClickListener {
    boolean edit;
    private EditText nameEditText;
    private EditText ISBNEditText;
    private EditText priceEditText;
    private Spinner spinner;
    private long rowID;
    private Button submitBtn;
    DatabaseReference db;
    String userID;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        db = FirebaseDatabase.getInstance().getReference("books");
        mAuth = FirebaseAuth.getInstance();
        nameEditText = (EditText) findViewById(R.id.nameEditText);
       ISBNEditText = (EditText) findViewById(R.id.ISBNEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        submitBtn = (Button) findViewById(R.id.submitBtn);

        spinner= (Spinner) findViewById(R.id.spinner);
        Bundle extras = getIntent().getExtras(); // get Bundle of extras previously added with intent putExtra()
        edit = false;
        int selection=0;
        // if there are extras, use them to populate the EditTexts
        if (extras != null)
        {
            rowID = extras.getLong("row_id");
            nameEditText.setText(extras.getString("name"));
           ISBNEditText.setText(extras.getString("ISBN"));
            priceEditText.setText(extras.getString("price"));
            String genre = extras.getString("genre");
            Log.d("TOD", "onCreate: "+genre);

            switch (genre){
                case "Fantasy":
                    selection = 0;
                    break;
                case "Science fiction":
                    selection = 1;
                    break;
                case "Romance":
                    selection = 2;
                    break;
                case "Thriller":
                    selection = 3;
                    break;
                case "Mystery":
                    selection = 4;
                    break;
                case "Horror":
                    selection = 5;
                    break;
                default:
                    selection = 1;
            }
            Log.d("TOD", "onCreate: "+selection);

            edit = true;
        } // end if

        //fill the spinner

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinner_data,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selection);
        submitBtn.setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null) {
            Log.d("frin shop", "onStart: "+currentUser.getUid());
            userID = currentUser.getUid();
        }
        else{
            startActivity(new Intent(
                    getApplicationContext(),
                    LoginActivity.class));
            finish();
        }
    }
    @Override
    public void onClick(View v) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo =
                connectivityManager.getActiveNetworkInfo();

// if network is connected, download feed

        if (networkInfo != null && networkInfo.isConnected()) {

            final String price;
            final String name,ISBN;
            final String genre;
            price = priceEditText.getText().toString();
            name = nameEditText.getText().toString();
            ISBN = ISBNEditText.getText().toString();
            genre = (String) spinner.getSelectedItem();
            Log.d("TAG", "onClick: "+genre);
            if(edit){
                try {
                    Book newBook = new Book(name,userID,ISBN,genre,price,rowID);
                    db.child(Long.toString(rowID)).setValue(newBook);//added
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                long uniqueID = currentTimeMillis();
                Book newBook = new Book(name,userID,ISBN,genre,price,uniqueID);
                db.child(Long.toString(uniqueID)).setValue(newBook);//added


            }
            finish();
        }
        else
            Toast.makeText(getApplicationContext(), "You need an internet connection for this action", Toast.LENGTH_LONG).show();

    }
}
