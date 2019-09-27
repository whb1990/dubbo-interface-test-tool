package com.whb.dubbo.context;

import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.rpc.RpcResult;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 转发响应
 */
public class ResponseDispatcher {

    private Map<Long, CompletableFuture> futures = new ConcurrentHashMap<>();

    private ResponseDispatcher() {

    }

    public CompletableFuture<RpcResult> getFuture(Request req) {
        return futures.get(req.getId());
    }

    public void register(Request req) {
        CompletableFuture future = new CompletableFuture();
        futures.put(req.getId(), future);
    }

    public void dispatch(Response res) {
        CompletableFuture future = futures.get(res.getId());
        if (null == future) {
            throw new RuntimeException();
        }
        future.complete(res.getResult());
    }

    public CompletableFuture removeFuture(Request req) {
        return futures.remove(req.getId());
    }

    static class ResponseDispatcherHolder {
        static final ResponseDispatcher instance = new ResponseDispatcher();
    }

    public static ResponseDispatcher getDispatcher() {
        return ResponseDispatcherHolder.instance;
    }
}
