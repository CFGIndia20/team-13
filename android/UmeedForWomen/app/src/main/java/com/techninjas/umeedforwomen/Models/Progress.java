package com.techninjas.umeedforwomen.Models;

public class Progress {
    private int id;
    private String taskId;
    private String timestamp;
    private int qty;
    private String imageLocation;

    public Progress(int id, String taskId, int qty, String timestamp, String imageLocation){
        this.id = id;
        this.taskId = taskId;
        this.timestamp = timestamp;
        this.qty = qty;
        this.imageLocation = imageLocation;
    }

    public Progress(String taskId, int qty, String timestamp, String imageLocation){
        this.id = -1;
        this.taskId = taskId;
        this.timestamp = timestamp;
        this.qty = qty;
        this.imageLocation = imageLocation;
    }

    public Progress(String taskId, int qty, String timestamp){
        this.id = -1;
        this.taskId = taskId;
        this.timestamp = timestamp;
        this.qty = qty;
        this.imageLocation = "";
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }
}
