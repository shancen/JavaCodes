package io.github.kimmking.gateway.outbound.okhttp;

import io.github.kimmking.gateway.filter.HeaderHttpResponseFilter;
import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.filter.HttpResponseFilter;
import io.github.kimmking.gateway.outbound.httpclient4.NamedThreadFactory;
import io.github.kimmking.gateway.router.HttpEndpointRouter;
import io.github.kimmking.gateway.router.RandomHttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler {
    private OkHttpClient okHttpClient;
    private ExecutorService proxyService;
    private List<String> backendUrls;

    HttpResponseFilter filter = new HeaderHttpResponseFilter();
    HttpEndpointRouter router = new RandomHttpEndpointRouter();

    public OkhttpOutboundHandler(List<String> backends) {
        this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());
//        int cores = Runtime.getRuntime().availableProcessors();
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        proxyService = new ThreadPoolExecutor(4, 10,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);

        this.okHttpClient = new OkHttpClient();
    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter filter) {
        String backendUrl = router.route(this.backendUrls);
        final String url = backendUrl + fullRequest.uri();
        filter.filter(fullRequest, ctx);
        proxyService.submit(()->fetchGet(fullRequest, ctx, url));

    }

    private String formatUrl(String backend) {
        return backend.endsWith("/")?backend.substring(0,backend.length()-1):backend;
    }

    private void fetchGet(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final String url) {
        DefaultFullHttpResponse defaultFullHttpResponse=null;
        Request request = new Request.Builder()
                .url(url)
                .addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE)
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            byte[] body = response.body().bytes();
            System.out.println(new String(body));
            System.out.println(body.length);
            defaultFullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            defaultFullHttpResponse.headers().set("Content-Type", "application/json");
            defaultFullHttpResponse.headers().setInt("Content-Length", response.header("Content-Length").length());
            filter.filter(defaultFullHttpResponse);
        } catch (IOException e) {
            e.printStackTrace();
            defaultFullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        }finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(defaultFullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(defaultFullHttpResponse);
                }
            }
            ctx.flush();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
