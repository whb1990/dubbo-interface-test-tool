package com.whb.dubbo.model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 用例实体对象
 */
@Data
public class CaseModel {

    /**
     * case Id.
     */
    private long caseId;
    /**
     * case group.
     */
    private String caseGroup;
    private String caseName;
    private String caseDesc;
    private String insertTime;
    /**
     * provider address.
     */
    private String address;
    private String interfaceName;
    /**
     * the method name with parameters.
     */
    private String methodText;
    private String providerKey;
    private String methodKey;
    /**
     * parameters.
     */
    private String json;
    /**
     * assert condition.
     */
    private String condition;
    /**
     * expected result.
     */
    private String expect;

}
