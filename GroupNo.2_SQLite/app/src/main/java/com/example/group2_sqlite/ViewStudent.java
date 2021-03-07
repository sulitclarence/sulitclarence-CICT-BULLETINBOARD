package com.example.group2_sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class ViewStudent extends AppCompatActivity {


    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        List<StudentClass> studentClasses = dataBaseHelper.getStudentList();

        if (studentClasses.size() > 0){
            StudentAdapter studentAdapter = new StudentAdapter(studentClasses,ViewStudent.this);
            recyclerView.setAdapter(studentAdapter);
        }else {
            Toast.makeText(this, "There is no student in the database", Toast.LENGTH_SHORT).show();
        }




    }
}
