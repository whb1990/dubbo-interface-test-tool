package com.whb.dubbo.model;

import lombok.Data;

@Data
public class PointModel {

    private String ip;
    private int port;

    public PointModel(String host, Integer port) {
        this.ip = host;
        this.port = port;
    }
}
