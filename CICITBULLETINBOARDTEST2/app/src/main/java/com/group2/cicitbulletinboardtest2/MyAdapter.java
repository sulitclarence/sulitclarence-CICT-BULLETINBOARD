package com.group2.cicitbulletinboardtest2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends ArrayAdapter<AdminData> {

    Context context;
    List<AdminData> arrayListAdmin;
    public MyAdapter(@NonNull Context context, List<AdminData> arrayListAdmin) {
        super(context, R.layout.admin_list_items, arrayListAdmin);
        this.context = context;
        this.arrayListAdmin = arrayListAdmin;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_list_items, null, true);
        TextView tv_id = view.findViewById(R.id.id);
        TextView tv_title = view.findViewById(R.id.text_title);
        TextView tv_date = view.findViewById(R.id.text_date);
        TextView tv_time = view.findViewById(R.id.text_time);

        tv_id.setText(arrayListAdmin.get(position).getId());
        tv_title.setText(arrayListAdmin.get(position).getTitle());
        tv_date.setText(arrayListAdmin.get(position).getDate());
        tv_time.setText(arrayListAdmin.get(position).getTime());
        return view;
    }
}
