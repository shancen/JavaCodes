package io.github.kimmking.gateway.client;

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
        HttpGet httpGet = new HttpGet("http://localhost:8888");
        httpGet.setHeader("liu","zhongshan");
        try ( CloseableHttpResponse response = httpclient.execute(httpGet)) {

            HttpEntity entity = response.getEntity();
            System.out.println("ResponseFilter >>>>>>>>>>>>>>"+response.getFirstHeader("kk").getValue());
            System.out.println("ResponseFilter >>>>>>>>>>>>>>"+response.getFirstHeader("set-cookie").getValue());
            String s = EntityUtils.toString(entity);
            System.out.println(">>>>>>>>>>>>>>" + s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
