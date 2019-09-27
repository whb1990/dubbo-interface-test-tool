package com.whb.dubbo.model;

import com.alibaba.dubbo.common.utils.StringUtils;
import lombok.Data;

/**
 * @author Joey
 * @date 2018/6/16 9:55
 */
@Data
public class PomModel {

    private String groupId;
    private String artifactId;
    private String version;
    private String scope;

    public boolean isBroken() {
        return StringUtils.isEmpty(groupId) || StringUtils.isEmpty(artifactId) || StringUtils.isEmpty(version);
    }
}
