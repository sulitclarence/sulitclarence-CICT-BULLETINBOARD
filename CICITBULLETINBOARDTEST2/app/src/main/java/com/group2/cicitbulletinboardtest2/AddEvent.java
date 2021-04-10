package com.group2.cicitbulletinboardtest2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;

import android.content.DialogInterface;

import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import static com.android.volley.Response.*;


public class AddEvent extends AppCompatActivity {

    Calendar calendar;
    TextView toolbar_title;
    EditText edit_title, edit_description;
    TextView date_text;
    Button publish_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        calendar = new GregorianCalendar();
        toolbar_title = findViewById(R.id.toolbar_title);
        edit_title = findViewById(R.id.edit_title);
        edit_description = findViewById(R.id.edit_description);
        date_text = findViewById(R.id.date_text);
        publish_btn = findViewById(R.id.publish_btn);

        date_text.setText(new SimpleDateFormat("YYYY-MM-dd").format(calendar.getTime()));

        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertdata();
            }
        });
    }




    private void insertdata() {
        final String title = edit_title.getText().toString().trim();
        final String description = edit_description.getText().toString().trim();
        final String date = date_text.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
            return;
        } else if (description.isEmpty()) {
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
            return;
        } else {


            StringRequest request = new StringRequest(Request.Method.POST, "https://cictbulletinboard.000webhostapp.com/insert.php",
                    response -> {
                        if (response.equalsIgnoreCase("Data Inserted")) {
                            Toast.makeText(AddEvent.this, "Event Added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddEvent.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(AddEvent.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }, error -> Toast.makeText(AddEvent.this, error.getMessage(), Toast.LENGTH_SHORT).show()
            ) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("title", title);
                    params.put("description", description);
                    params.put("date", date);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(AddEvent.this);
            requestQueue.add(request);
        }
    }

    public void chooseDate(View view) {
        final View dialogView = View.inflate(this, R.layout.datepicker, null);
        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Choose Date");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                date_text.setText(new SimpleDateFormat("YYYY-MM-dd").format(calendar.getTime()));

            }
        });
        builder.show();
    }
}



