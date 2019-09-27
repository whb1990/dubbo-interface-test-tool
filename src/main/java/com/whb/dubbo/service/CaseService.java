
package com.whb.dubbo.service;

import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.model.CaseModel;

import java.util.List;

public interface CaseService {

    /**
     * save the case.
     *
     * @param model
     * @return
     */
    ResultDTO<CaseModel> save(CaseModel model);

    /**
     * list all case.
     *
     * @return
     */
    List<Object> listAll();

}
