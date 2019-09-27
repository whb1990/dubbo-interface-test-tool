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
 * 客户端
 */
public class DoeClient extends TransportClient {

    public DoeClient(URL url) {
        super(url, new SendReceiveHandler());
    }

    public void doConnect() {
        ChannelFuture future = bootstrap.connect(getConnectAddress());
        boolean ret = future.awaitUninterruptibly(timeout, TimeUnit.MILLISECONDS);
        if (ret && future.isSuccess()) {
            Channel newChannel = future.getChannel();
            newChannel.setInterestOps(Channel.OP_READ_WRITE);
            DoeClient.this.channel = future.getChannel();
        } else {
            throw new RuntimeException("can't not connect to server.");
        }
    }

    public void send(Request req) throws RemotingException {

        NettyChannel ch = NettyChannel.getOrAddChannel(this.channel, url, handler);

        ch.send(req);
    }
}
