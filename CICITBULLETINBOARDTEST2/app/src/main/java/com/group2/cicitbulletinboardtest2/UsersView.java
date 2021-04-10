package com.group2.cicitbulletinboardtest2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersView extends AppCompatActivity {

    ListView listView;
    MyAdapter adapter;
    public static ArrayList<AdminData> adminDataArrayList = new ArrayList<>();
    String url = "https://cictbulletinboard.000webhostapp.com/retrieve.php";
    AdminData adminData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_view);

        listView = findViewById(R.id.myListView1);
        adapter = new MyAdapter(this, adminDataArrayList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                CharSequence[] dialogItem = {"View"};
                builder.setTitle(adminDataArrayList.get(position).getTitle());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        if (i == 0) {
                            startActivity(new Intent(getApplicationContext(), UsersViewForDetails.class)
                                    .putExtra("position", position));
                        }
                    }
                });
                builder.create().show();

            }
        });
        retrieveData();

    }

    public void retrieveData() {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    adminDataArrayList.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("admin");

                        if (success.equals("1")) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String title = object.getString("title");
                                String description = object.getString("description");
                                String date = object.getString("date");

                                adminData = new AdminData(id, title, description, date);
                                adminDataArrayList.add(adminData);
                                adapter.notifyDataSetChanged();


                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(UsersView.this, error.getMessage(), Toast.LENGTH_SHORT).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}