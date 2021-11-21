package io.github.kimmking.gateway.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpTest {
    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8888")
                .addHeader("liu","zhongshan1")
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            System.out.println(response.body().string());
            System.out.println("ResponseFilter >>>>>>>>>>>>>>"+response.header("kk"));
            System.out.println("ResponseFilter >>>>>>>>>>>>>>"+response.header("set-cookie"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
