package com.example.shookisha.shared;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Api {
    private static final String baseUrl = "http://37.187.89.224:8081/shookisha/api/";
    private static final String baseUrlImg = "http://www.worldcorpgroup.fr/shookisha/picture/offers/";
    private static final String baseUrlCoupon = "http://www.worldcorpgroup.fr/shookisha/picture/coupons_shook/";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    public String getBaseUrlImg(){
        return this.baseUrlImg;
    }

    public String getBaseUrlCoupon(){
        return this.baseUrlCoupon;
    }

    public String post(String ressource, String data) throws IOException {
        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .url(baseUrl+ressource)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String get(String ressource) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl+ressource)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String postAuth(String ressource,String data, String auth)throws IOException {
        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .url(baseUrl+ressource)
                .post(body)
                .header("Authorization",auth)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String getAuth(String ressource, String auth) throws IOException {
        Request request = new Request.Builder()
                .url(baseUrl+ressource)
                .header("Authorization",auth)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String put(String ressource, String data) throws IOException {
        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder()
                .url(baseUrl+ressource)
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }



}
