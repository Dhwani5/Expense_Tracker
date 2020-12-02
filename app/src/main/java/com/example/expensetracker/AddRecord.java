package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class AddRecord extends AppCompatActivity {

    private FirebaseFirestore db;

    EditText title,amount;
    Spinner type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        db = FirebaseFirestore.getInstance();
        title= findViewById(R.id.title);
        amount= findViewById(R.id.amount);
        type= findViewById(R.id.type);


        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord();
            }
        });


    }

    private void addRecord() {
        AsyncHttpClient client  = new AsyncHttpClient();

        RequestParams product = new RequestParams();
        product.put("title", title.getText().toString());
        product.put("amount", amount.getText().toString());
        product.put("type", type.getSelectedItem().toString());
        product.put("timestamp", FieldValue.serverTimestamp());
        client.post(getResources().getString(R.string.url)+"addRecord",product,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(AddRecord.this, "Added", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(AddRecord.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
