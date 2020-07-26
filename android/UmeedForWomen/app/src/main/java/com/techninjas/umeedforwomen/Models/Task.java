package com.techninjas.umeedforwomen.Models;

import com.google.gson.annotations.SerializedName;

public class Task {
    @SerializedName("_id")
    private String id;
    @SerializedName("task_name")
    private String task_name;
    @SerializedName("quantity")
    private int qty;
    @SerializedName("currentQuantity")
    private int currentQty;

    private int done = 0;

    public Task(String id, String task_name, int qty){
        this.id = id;
        this.task_name = task_name;
        this.qty = qty;
        this.done = 0;
    }

    public Task(String id, String task_name, int qty, int done){
        this.id = id;
        this.task_name = task_name;
        this.qty = qty;
        this.done = done;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
