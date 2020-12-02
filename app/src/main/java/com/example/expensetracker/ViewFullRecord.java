package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ViewFullRecord extends AppCompatActivity {

    TextView title,amount,balance;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_record);
        title=findViewById(R.id.title);
        amount=findViewById(R.id.amount);
        balance=findViewById(R.id.balance);
        amount.setText(getIntent().getStringExtra("amount"));
        title.setText(getIntent().getStringExtra("title"));
        balance.setText(getIntent().getStringExtra("balance"));

        db = FirebaseFirestore.getInstance();
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord(getIntent().getStringExtra("ID"));
            }
        });
    }

    private void deleteRecord(String id) {


        AsyncHttpClient client  = new AsyncHttpClient();

        RequestParams product = new RequestParams();
        product.put("id", id);

        client.post(getResources().getString(R.string.url)+"removeRecord",product,new TextHttpResponseHandler(){


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ViewFullRecord.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(ViewFullRecord.this, "Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}
