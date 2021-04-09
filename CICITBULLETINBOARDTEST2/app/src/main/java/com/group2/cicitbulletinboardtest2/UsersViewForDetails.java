package com.group2.cicitbulletinboardtest2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UsersViewForDetails extends AppCompatActivity {

    TextView tvtitle, tvdescription, tvdate, tvid;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_details);

        tvid = findViewById(R.id.id_details);
        tvtitle = findViewById(R.id.title_details);
        tvdescription = findViewById(R.id.description_details);
        tvdate = findViewById(R.id.date_details);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        tvid.setText(UsersView.adminDataArrayList.get(position).getId());
        tvtitle.setText(UsersView.adminDataArrayList.get(position).getTitle());
        tvdescription.setText(UsersView.adminDataArrayList.get(position).getDescription());
        tvdate.setText(UsersView.adminDataArrayList.get(position).getDate());
    }
}