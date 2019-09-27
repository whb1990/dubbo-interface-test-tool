package com.whb.dubbo.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单节点
 */
@Data
public class MenuNode extends MenuTree implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 子菜单
     */
    private List<MenuNode> children = new ArrayList<>();

}
