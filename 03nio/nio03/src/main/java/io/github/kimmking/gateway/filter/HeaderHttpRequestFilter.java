package io.github.kimmking.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;


public class HeaderHttpRequestFilter implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        String filter = fullRequest.headers().get("liu");
        if ("zhongshan".equals(filter)){
            System.out.println("延长token时长>>>>>>>>>>");
        }else{
            System.out.println("普通用户>>>>>>>>>>");
        }
        fullRequest.headers().set("mao", "soul");
    }
}
