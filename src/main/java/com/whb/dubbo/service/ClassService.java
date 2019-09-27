package com.whb.dubbo.service;

import com.whb.dubbo.dto.ConnectDTO;
import com.whb.dubbo.dto.MethodModelDTO;
import com.whb.dubbo.dto.ResultDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Joey
 * @date 2018/6/28 11:28
 */
public interface ClassService {

    /**
     * generate the simple json string of the method parameters.
     *
     * @param dto
     * @return
     */
    ResultDTO<String> generateMethodParamsJsonString(@NotNull MethodModelDTO dto) throws ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * get all methods from the given interface.
     *
     * @param dto
     * @return
     */
    List<MethodModelDTO> listMethods(ConnectDTO dto);

}
