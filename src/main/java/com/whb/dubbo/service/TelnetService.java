package com.whb.dubbo.service;

import com.whb.dubbo.dto.ConnectDTO;
import com.whb.dubbo.dto.ResultDTO;

import javax.validation.constraints.NotNull;

public interface TelnetService {

    /**
     * send message with telnet client.
     *
     * @param dto
     * @return
     */
    ResultDTO<String> send(@NotNull ConnectDTO dto);
}
