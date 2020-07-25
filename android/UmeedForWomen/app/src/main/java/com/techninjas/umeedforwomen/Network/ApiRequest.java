package com.techninjas.umeedforwomen.Network;

import java.util.List;

import retrofit2.Callback;

public class ApiRequest {
    private static ApiRequest instance;

    public static ApiRequest getInstance() {
        if (instance == null) {
            instance = new ApiRequest();
        }
        return instance;
    }

//    public void loginUser(String phone, String password, Callback<List<LoginInfo>> callback) {
//        ApiClient.getClient().create(ApiInterface.class).loginUser(phone, password, Constants.API_KEY).enqueue(callback);
//    }
}
