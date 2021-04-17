package com.group2.cicitbulletinboardtest2;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class UpdateAdminData extends AppCompatActivity {
    EditText update_title, update_description;
    TextView update_date_text, update_id, update_timer;
    Calendar update_calendar;
    TextView update_toolbar_title;
    Button update_btn;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_admin_data);

        Calendar c1 = Calendar.getInstance();
        update_calendar = new GregorianCalendar();
        update_title = findViewById(R.id.update_title);
        update_toolbar_title = findViewById(R.id.update_toolbar_title);
        update_description = findViewById(R.id.update_description);
        update_date_text = findViewById(R.id.update_date_text);
        update_btn = findViewById(R.id.update_btn);
        update_id = findViewById(R.id.update_id);
        update_timer = findViewById(R.id.update_timer);

        update_date_text.setText(new SimpleDateFormat("YYYY-MM-dd").format(update_calendar.getTime()));
        SimpleDateFormat format1 = new SimpleDateFormat("k:mm a");
        String time2 = format1.format(c1.getTime());
        update_timer.setText(time2);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");


        update_id.setText(MainActivity.adminDataArrayList.get(position).getId());
        update_title.setText(MainActivity.adminDataArrayList.get(position).getTitle());
        update_description.setText(MainActivity.adminDataArrayList.get(position).getDescription());

    }

    public void btn_update(View view) {
        String id = update_id.getText().toString();
        String title = update_title.getText().toString();
        String description = update_description.getText().toString();
        String date = update_date_text.getText().toString();
        String time = update_timer.getText().toString();


        StringRequest request = new StringRequest(Request.Method.POST, "https://cictbulletinboard.000webhostapp.com/update.php",
        new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Toast.makeText(UpdateAdminData.this, "Event Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateAdminData.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }){

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("id",id);
                params.put("title",title);
                params.put("description",description);
                params.put("date",date);
                params.put("time",time);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UpdateAdminData.this);
        requestQueue.add(request);
    }
    public void chooseDate1(View view) {
        final View dialogView = View.inflate(this, R.layout.datepicker, null);
        final DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        datePicker.updateDate(update_calendar.get(Calendar.YEAR), update_calendar.get(Calendar.MONTH), update_calendar.get(Calendar.DAY_OF_MONTH));


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Choose Date");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                update_calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                update_date_text.setText(new SimpleDateFormat("YYYY-MM-dd").format(update_calendar.getTime()));

            }
        });
        builder.show();
    }
    public void chooseTimer1(View view) {
        Calendar calendar1 = Calendar.getInstance();
        int hours = calendar1.get(Calendar.HOUR_OF_DAY);
        int mins = calendar1.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateAdminData.this, R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hoursOfday, int minute) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, hoursOfday);
                c.set(Calendar.MINUTE, minute);
                c.setTimeZone(TimeZone.getDefault());
                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                String time1 = format.format(c.getTime());
                update_timer.setText(time1);
            }

        }, hours, mins,true);
        timePickerDialog.show();

    }

}