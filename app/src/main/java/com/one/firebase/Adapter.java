package com.one.firebase;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Adapter extends ArrayAdapter<UserHelper> {
    Context curview;
    public Adapter(@NonNull Context context, int resource, List<UserHelper> objects) {
        super(context, resource,objects);
        curview=context;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.adapter, parent, false);
        }
        UserHelper message = getItem(position);
        ImageView photoImageView = convertView.findViewById(R.id.imageView);
        TextView nameTextView =  convertView.findViewById(R.id.text);
        TextView cityTextView =  convertView.findViewById(R.id.city);


        photoImageView.setImageResource(R.mipmap.ic_launcher_round);
        nameTextView.setText(message.getUser_name());
        cityTextView.setText(message.getCity());
        convertView.setOnClickListener(view -> {
            Intent intent=new Intent(curview.getApplicationContext(),display_Data.class);
            intent.putExtra("name",message.getUser_name());

            intent.putExtra("city",message.getCity());
            intent.putExtra("age",message.getAge());
            intent.putExtra("gender",message.getGender());
            getContext().startActivity(intent);

        });



        return convertView;
    }
}
/*Intent intent=new Intent(((Activity)getContext()),display_Data.class);
                intent.putExtra("name",message.getUser_name());
                intent.putExtra("city",message.getCity());
                intent.putExtra("age",message.getAge());
                intent.putExtra("gender",message.getGender());*/
