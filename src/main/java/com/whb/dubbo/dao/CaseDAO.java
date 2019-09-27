package com.whb.dubbo.dao;

import com.whb.dubbo.model.CaseModel;

import java.util.List;

/**
 * 用例管理接口定义
 */
public interface CaseDAO {

    /**
     * 保存
     *
     * @param model
     * @return
     */
    int save(CaseModel model);

    /**
     * 列表
     *
     * @return
     */
    List<CaseModel> listAll();

}
