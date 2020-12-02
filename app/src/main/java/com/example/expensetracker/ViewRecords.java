package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ViewRecords extends AppCompatActivity {

    FirebaseFirestore db;

    CustomAdapter customAdapter;


    ArrayList<Record> arrayList ;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);
        db = FirebaseFirestore.getInstance();
        arrayList= new ArrayList();
        customAdapter = new CustomAdapter(this, arrayList);
        list = findViewById(R.id.list);
        list.setAdapter(customAdapter);






        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),AddRecord.class);
                startActivity(i);
            }
        });

    }

    private void getData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getResources().getString(R.string.url)+"getRecords",new RequestParams(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    setAdapter(response.getJSONArray("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setAdapter(JSONArray value) throws JSONException {
        arrayList.clear();
        for (int i =0;i<value.length();i++) {
            JSONObject data = value.getJSONObject(i);
            Record u = new Record();
            u.setID(data.getString("id"));
            u.setTitle(data.getString("title"));
            u.setAmount(data.getString("amount"));
            u.setType(data.getString("type"));

            arrayList.add(u);
        }
        Log.d("arrayList",arrayList.size()+"");
        setUpBalace(arrayList);
        customAdapter.notifyDataSetChanged();
    }


    private void setUpBalace(List<Record> records) {
        double cumulativeTotal = 0;
        for(int i=0;i<records.size();i++){

            try {
                if(records.get(i).getType().equals("credit")){
                    cumulativeTotal+=Double.parseDouble(records.get(i).getAmount());
                }else{
                    cumulativeTotal-=Double.parseDouble(records.get(i).getAmount());
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            records.get(i).setBalance(String.valueOf(cumulativeTotal));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
