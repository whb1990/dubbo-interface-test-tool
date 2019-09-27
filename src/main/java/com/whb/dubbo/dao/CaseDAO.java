package com.whb.dubbo.dao;

import com.whb.dubbo.model.CaseModel;

import java.util.List;

/**
 * @author Joey
 * @date 2018/6/29 15:36
 */
public interface CaseDAO {

    /**
     * save the case.
     *
     * @param model
     * @return
     */
    int save(CaseModel model);

    /**
     * list all model.
     *
     * @return
     */
    List<CaseModel> listAll();

}
