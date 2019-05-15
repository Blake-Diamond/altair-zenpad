package com.example.blake.zenpad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ActivityPreset extends AppCompatActivity {

    final Intent preset_Intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset);


    }














    public void toHome(View view){
        Intent presetMassage = new Intent(ActivityPreset.this, MainActivity.class);
        startActivity(presetMassage);
    }

    public void toSettings1(View view){
        Intent presetMassage = new Intent(ActivityPreset.this, ActivitySettings.class);
        startActivity(presetMassage);
    }


}
