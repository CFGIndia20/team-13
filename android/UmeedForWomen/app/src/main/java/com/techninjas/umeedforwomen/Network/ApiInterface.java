package com.techninjas.umeedforwomen.Network;

import com.techninjas.umeedforwomen.Models.Task;
import com.techninjas.umeedforwomen.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("women/login")
    Call<User> loginUser(@Field("phone") String phone,
                               @Field("password") String password);
                               //@Field("api_key") String api_key);
    @FormUrlEncoded
    @POST("task/woman")
    Call<List<Task>> fetchTasks(@Field("womanId") String id);
    @FormUrlEncoded
    @POST("task/upload")
    Call<Void> uploadProgress(@Field("taskId") String id, @Field("currentQuantity") int qty, @Field("timestamp") String timestamp,
                              @Field("image") String image, @Field("imageType") String image_type);
}
