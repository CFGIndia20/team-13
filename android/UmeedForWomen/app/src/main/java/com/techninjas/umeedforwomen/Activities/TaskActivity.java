package com.techninjas.umeedforwomen.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.fluidslider.FluidSlider;
import com.techninjas.umeedforwomen.R;
import com.techninjas.umeedforwomen.Utils.Constants;

import java.util.Objects;


public class TaskActivity extends AppCompatActivity {
    private TextView nameView;
    private Button saveButton;
    private FluidSlider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        saveButton = findViewById(R.id.saveButton);
        slider = findViewById(R.id.slider);
        nameView = findViewById(R.id.welcome);

        String name = "XYZ";
        nameView.setText("Welcome, " + name + "!");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProgess();
            }
        });
    }

    private void saveProgess(){
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