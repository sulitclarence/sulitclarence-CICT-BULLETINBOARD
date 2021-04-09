package com.group2.cicitbulletinboardtest2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    ListView listView;
    MyAdapter adapter;
    public static ArrayList<AdminData> adminDataArrayList = new ArrayList<>();
    String url = "https://cictbulletinboard.000webhostapp.com/retrieve.php";
    AdminData adminData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.myListView);
        adapter = new MyAdapter(this, adminDataArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                CharSequence[] dialogItem = {"View","Update","Delete"};
                builder.setTitle(adminDataArrayList.get(position).getTitle());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        switch (i){
                            case 0:
                                startActivity(new Intent(getApplicationContext(),AdminDetails.class)
                                .putExtra("position", position));
                            break;
                            case 1:
                                startActivity(new Intent(getApplicationContext(),UpdateAdminData.class)
                                .putExtra("position",position));
                                finish();
                            break;
                            case 2:
                                deleteData(adminDataArrayList.get(position).getId());
                            break;
                        }
                    }
                });
                builder.create().show();

            }


        });

        retrieveData();

    }
    private void deleteData(final String id) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://cictbulletinboard.000webhostapp.com/delete.php"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    if(response.equalsIgnoreCase("Data Deleted")){
                        Toast.makeText(MainActivity.this, "Event Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Event Not Deleted", Toast.LENGTH_SHORT).show();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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
                        }, error -> Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()
                );
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(request);
            }

            public void openAddEvent(View view) {
                startActivity(new Intent(this, AddEvent.class));
            }

        }