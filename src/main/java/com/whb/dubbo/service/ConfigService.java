package com.whb.dubbo.service;

import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.model.RegistryModel;

import java.util.List;

public interface ConfigService {

    /**
     * list all registry.
     *
     * @return
     */
    List<RegistryModel> listRegistry();

    /**
     * add registry.
     *
     * @return
     */
    ResultDTO<RegistryModel> addRegistry(RegistryModel model);

    /**
     * load zk config.
     */
    void loadZkConfigFromResource();

    /**
     * delete registry.
     *
     * @param model
     * @return
     */
    ResultDTO<RegistryModel> delRegistry(RegistryModel model);
}
