package com.techninjas.umeedforwomen.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.fluidslider.FluidSlider;
import com.techninjas.umeedforwomen.R;
import com.techninjas.umeedforwomen.Utils.Constants;
import com.techninjas.umeedforwomen.Utils.SharedPrefUtil;

import java.util.Objects;


public class TaskActivity extends AppCompatActivity {
    private TextView nameView;
    private Button saveButton, photoButton, uploadButton;
    ImageButton logoutButton;
    private FluidSlider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        saveButton = findViewById(R.id.saveButton);
        photoButton = findViewById(R.id.photoButton);
        uploadButton = findViewById(R.id.uploadButton);
        logoutButton = findViewById(R.id.logoutButton);
        slider = findViewById(R.id.slider);
        nameView = findViewById(R.id.welcome);

        String name = SharedPrefUtil.getName(this);
        nameView.setText("Welcome, " + name + "!");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProgress();
            }
        });
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskActivity.this, ImageActivity.class));
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefUtil.removeName(TaskActivity.this);
                startActivity(new Intent(TaskActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void saveProgress(){
        Float progess = slider.getPosition();
        Integer max = Integer.parseInt(Objects.requireNonNull(slider.getEndText()));
        int curr = (int) (progess * max);
        logger(Integer.toString(curr));
        toast("Saved!");
    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void logger(String msg){
        Log.d(Constants.LOG, msg);
    }
}