package com.whb.dubbo.handler;

import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.fastjson.JSON;
import com.whb.dubbo.context.ResponseDispatcher;
import lombok.extern.slf4j.Slf4j;

/**
 * nio event listener.
 */
@Slf4j
public class SendReceiveHandler implements ChannelHandler {

    @Override
    public void connected(Channel channel) throws RemotingException {
        log.info("SendReceiveHandler.connected");
    }

    @Override
    public void disconnected(Channel channel) throws RemotingException {
        log.info("SendReceiveHandler.disconnected");
    }

    /**
     * 发送消息
     *
     * @param channel
     * @param message
     * @throws RemotingException
     */
    @Override
    public void sent(Channel channel, Object message) throws RemotingException {
        log.info("SendReceiveHandler.sent");
        if (message instanceof Request) {
            Request req = (Request) message;
            ResponseDispatcher.getDispatcher().register(req);
        }
    }

    /**
     * 接收响应消息
     *
     * @param channel
     * @param message
     */
    @Override
    public void received(Channel channel, Object message) {
        log.info("SendReceiveHandler.received({})", JSON.toJSONString(message));
        if (message instanceof Response) {
            Response res = (Response) message;
            if (res.getStatus() == Response.OK) {
                try {
                    if (res.getResult() instanceof RpcResult) {
                        ResponseDispatcher.getDispatcher().dispatch(res);
                    }
                } catch (Exception e) {
                    log.error("callback invoke error .result:" + res.getResult() + ",url:" + channel.getUrl(), e);
                }
            } else if (res.getStatus() == Response.CLIENT_TIMEOUT || res.getStatus() == Response.SERVER_TIMEOUT) {
                try {
                    TimeoutException te = new TimeoutException(res.getStatus() == Response.SERVER_TIMEOUT, channel, res.getErrorMessage());
                } catch (Exception e) {
                    log.error("callback invoke error ,url:" + channel.getUrl(), e);
                }
            } else {
                try {
                    RuntimeException re = new RuntimeException(res.getErrorMessage());
                } catch (Exception e) {
                    log.error("callback invoke error ,url:" + channel.getUrl(), e);
                }
            }
        }
    }

    /**
     * 捕获异常
     *
     * @param channel
     * @param exception
     * @throws RemotingException
     */
    @Override
    public void caught(Channel channel, Throwable exception) throws RemotingException {
        log.error("SendReceiveHandler.caught", exception);
    }
}
