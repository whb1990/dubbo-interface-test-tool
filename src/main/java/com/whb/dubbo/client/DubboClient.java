package com.whb.dubbo.client;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.whb.dubbo.channel.NettyChannel;
import com.whb.dubbo.handler.SendReceiveHandler;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import java.util.concurrent.TimeUnit;


/**
 * 通道客户端
 */
public class DubboClient extends TransportClient {

    public DubboClient(URL url) {
        super(url, new SendReceiveHandler());
    }

    /**
     * 连接
     */
    public void doConnect() {
        ChannelFuture future = bootstrap.connect(getConnectAddress());
        boolean ret = future.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
        if (ret && future.isSuccess()) {
            Channel newChannel = future.getChannel();
            newChannel.setInterestOps(Channel.OP_READ_WRITE);
            DubboClient.this.channel = future.getChannel();
        } else {
            throw new RuntimeException("can not connect to server.");
        }
    }

    /**
     * 发送请求
     *
     * @param req
     * @throws RemotingException
     */
    public void send(Request req) throws RemotingException {

        NettyChannel ch = NettyChannel.getOrAddChannel(this.channel, url, handler);

        ch.send(req);
    }
}
