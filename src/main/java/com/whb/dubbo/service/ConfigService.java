package com.whb.dubbo.service;

import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.model.RegistryModel;

import java.util.List;

/**
 * @author Joey
 * @date 2018/7/9 19:40
 */
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
