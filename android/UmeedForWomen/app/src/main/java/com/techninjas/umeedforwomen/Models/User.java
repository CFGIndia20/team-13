package com.techninjas.umeedforwomen.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("phone")
    String mobileNo;

    @SerializedName("name")
    String name;

    @SerializedName("_id")
    String id;

    @SerializedName("skills")
    List<Skill> skills;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @SerializedName("district")
    District district;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
