package com.techninjas.umeedforwomen.Models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("aadhaar")
    String aadhaar;

    @SerializedName("name")
    String name;

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
