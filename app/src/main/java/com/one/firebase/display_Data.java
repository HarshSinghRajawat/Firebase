package com.one.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class display_Data extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display__data);
        //this will the the intent which will later help to get the data passed drom the apdater
        Intent i = getIntent();
        //Required View has being Initialized
        TextView nameTextView = findViewById(R.id.name);
        TextView cityTextView =  findViewById(R.id.dcity);
        TextView ageTextView =  findViewById(R.id.age);
        TextView genderTextView = findViewById(R.id.gender);

        //Here we are passing the String Data which was passed to this Intent (i.e. i)
        nameTextView.setText((String) i.getSerializableExtra("name"));
        cityTextView.setText((String) i.getSerializableExtra("city"));
        genderTextView.setText((String) i.getSerializableExtra("gender"));
        ageTextView.setText((String) i.getSerializableExtra("age"));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();

    }
}