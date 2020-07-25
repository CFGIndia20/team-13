package com.techninjas.umeedforwomen.Models;

public class Task {
    private String id;
    private String task_name;
    private int qty;

    public Task(String id, String task_name, int qty){
        this.id = id;
        this.task_name = task_name;
        this.qty = qty;
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
