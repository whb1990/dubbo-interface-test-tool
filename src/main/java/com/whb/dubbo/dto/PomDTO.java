package com.whb.dubbo.dto;

import lombok.Data;

/**
 * pom数据传输对象
 */
@Data
public class PomDTO extends BaseDTO {

    /**
     * pom.xml文件内容
     */
    private String pom;

    /**
     * pom.xml文件路径
     */
    private String path;

}
