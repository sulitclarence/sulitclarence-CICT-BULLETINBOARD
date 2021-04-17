package com.group2.cicitbulletinboardtest2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;

import android.app.TimePickerDialog;
import android.content.DialogInterface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.timepicker.TimeFormat;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import static com.android.volley.Response.*;
import static com.group2.cicitbulletinboardtest2.App.CHANNEL_1_ID;


public class AddEvent extends AppCompatActivity {

    Calendar calendar;
    TextView toolbar_title;
    EditText edit_title, edit_description;
    NotificationManagerCompat notificationManager;
    TextView date_text;
    Button publish_btn, btn_insert_image;
    TextView timer_text;
    ImageView insert_imageview;
    Bitmap bitmap;
    String encodedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Calendar c1 = Calendar.getInstance();
        calendar = new GregorianCalendar();
        toolbar_title = findViewById(R.id.toolbar_title);
        edit_title = findViewById(R.id.edit_title);
        edit_description = findViewById(R.id.edit_description);
        date_text = findViewById(R.id.date_text);
        timer_text = findViewById(R.id.timer_text);
        publish_btn = findViewById(R.id.publish_btn);
        btn_insert_image = findViewById(R.id.btn_insert_image);
        insert_imageview = findViewById(R.id.insert_imageview);




        date_text.setText(new SimpleDateFormat("YYYY-MM-dd").format(calendar.getTime()));
        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm aa");
        String time2 = format1.format(c1.getTime());
        timer_text.setText(time2);


        btn_insert_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    Dexter.withActivity(AddEvent.this)
                            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType("image/*");
                                        startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                                }
                            }).check();
            }
        });





        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertdata();
            }
        });
    }
    public void sendChannel(View v){
        String title = edit_title.getText().toString();
        String message = edit_description.getText().toString();
        Intent activityIntent = new Intent(this, AddEvent.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_announcement_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                .build();
        notificationManager.notify(1, notification);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1 && resultCode == RESULT_OK && data!=null){
                Uri filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                insert_imageview.setImageBitmap(bitmap);

                imageStore(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

        byte[] imageBytes = stream.toByteArray();
        encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);

    }

    private void insertdata() {
        final String title = edit_title.getText().toString().trim();
        final String description = edit_description.getText().toString().trim();
        final String date = date_text.getText().toString().trim();
        final String time = timer_text.getText().toString().trim();

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
                    params.put("time", time);
                    params.put("image",encodedImage);

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
    public void chooseTimer(View view) {
        Calendar calendar1 = Calendar.getInstance();
        int hours = calendar1.get(Calendar.HOUR_OF_DAY);
        int mins = calendar1.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddEvent.this, R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hoursOfday, int minute) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, hoursOfday);
                c.set(Calendar.MINUTE, minute);
                c.setTimeZone(TimeZone.getDefault());
                SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                String time1 = format.format(c.getTime());
                timer_text.setText(time1);
            }

        }, hours, mins,true);
        timePickerDialog.show();

    }

        }




