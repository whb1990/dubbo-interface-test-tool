package com.whb.dubbo.service;

import com.whb.dubbo.dto.ConnectDTO;
import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.dto.UrlModelDTO;
import com.whb.dubbo.model.ServiceModel;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ConnectService {

    /**
     * connect to zk and get all providers.
     *
     * @param conn
     * @return
     */
    List<ServiceModel> connect(@NotNull String conn) throws NoSuchFieldException, IllegalAccessException;

    /**
     * list providers of service.
     *
     * @param connect
     * @return
     */
    List<UrlModelDTO> listProviders(@NotNull ConnectDTO connect) throws NoSuchFieldException, IllegalAccessException;

    /**
     * send request to the real dubbo server.
     *
     * @param dto
     * @return
     */
    ResultDTO<String> send(ConnectDTO dto) throws Exception;
}
