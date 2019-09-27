package com.whb.dubbo.service;

import com.whb.dubbo.dto.ConnectDTO;
import com.whb.dubbo.dto.ResultDTO;

import javax.validation.constraints.NotNull;

/**
 * @author Joey
 * @date 2018/7/17 15:10
 */
public interface TelnetService {

    /**
     * send message with telnet client.
     * @param dto
     * @return
     */
    ResultDTO<String> send(@NotNull ConnectDTO dto);
}
