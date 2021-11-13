package com.lzs.nio.demo.nio;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientTest {


    public static void main(String[] args) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8803");
        try ( CloseableHttpResponse response = httpclient.execute(httpGet)) {

            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity);
            System.out.println(">>>>>>>>>>>>>>" + s);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
