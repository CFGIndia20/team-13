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
import com.techninjas.umeedforwomen.DB.TaskDBUtil;
import com.techninjas.umeedforwomen.Models.Task;
import com.techninjas.umeedforwomen.Models.User;
import com.techninjas.umeedforwomen.Network.ApiClient;
import com.techninjas.umeedforwomen.Network.ApiInterface;
import com.techninjas.umeedforwomen.R;
import com.techninjas.umeedforwomen.Utils.Constants;
import com.techninjas.umeedforwomen.Utils.SharedPrefUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class TaskActivity extends AppCompatActivity {
    private TextView nameView, taskNameView;
    private Button saveButton, photoButton, uploadButton;
    ImageButton logoutButton;
    private FluidSlider slider;
    private List<Task> tasks;
    TaskDBUtil db;

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
        taskNameView = findViewById(R.id.task_name);
        db = new TaskDBUtil(TaskActivity.this);

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
                SharedPrefUtil.removeAll(TaskActivity.this);
                startActivity(new Intent(TaskActivity.this, MainActivity.class));
                finish();
            }
        });

        getTasks();
    }

    private void getTasks(){
        tasks = db.readData();
        if(tasks.isEmpty()){
            dummy();
            //fetchAll();
            tasks = db.readData();
            if(!tasks.isEmpty()){
                updateUI();
            }
        }else{
            updateUI();
        }
    }

    private void updateUI(){
        Task task = tasks.get(0);
        //logger(task.getDone() + String.valueOf(task.getQty()));
        slider.setBubbleText(String.valueOf(task.getDone()));
        slider.setPosition((float)task.getDone() / (float)task.getQty());
        slider.setEndText(String.valueOf(task.getQty()));
        slider.setPositionListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float aFloat) {
                int comp = (int) (tasks.get(0).getQty() * aFloat);
                slider.setBubbleText(String.valueOf(comp));
                return null;
            }
        });
        taskNameView.setText(task.getTask_name());
    }

    private void dummy(){
        db.insertData(new ArrayList<Task>(Arrays.asList(new Task("1234", "Make a doll", 120))));
        db.insertData(new ArrayList<Task>(Arrays.asList(new Task("1235", "Make a toy", 100))));
        db.insertData(new ArrayList<Task>(Arrays.asList(new Task("1236", "Make a pot", 110))));
    }

    private void fetchAll(){
        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<Task>> call = apiInterface.fetchTasks(SharedPrefUtil.getId(this));
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if(response.isSuccessful()){
                    tasks = response.body();
                    db.insertData(tasks);
                }else{
                    toast("Unsuccessful request");
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                //toast();
            }
        });
    }

    private void saveProgress(){
        //Float progess = slider.getPosition();
        Integer max = Integer.parseInt(Objects.requireNonNull(slider.getEndText()));
        int curr = Integer.parseInt(Objects.requireNonNull(slider.getBubbleText()));
        logger(Integer.toString(curr));

        if(curr == max){
            db.deleteData(tasks.get(0).getId());
            toast("Great work!");
            getTasks();
        }else{
            db.update(tasks.get(0).getId(), curr);
            toast("Saved!");
        }
    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void logger(String msg){
        Log.d(Constants.LOG, msg);
    }
}