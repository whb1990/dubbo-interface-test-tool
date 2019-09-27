package com.whb.dubbo.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * 菜单树实体
 */
@Data
public class MenuTree implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private Integer uId;
    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 菜单ID
     */
    private Integer menuId;
    /**
     * 父级菜单
     */
    private Integer pmenuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单URL
     */
    private String menuUrl;
    /**
     * 菜单风格
     */
    private String menuStyle;
    /**
     * 菜单等级
     */
    private Integer mlevel;
    private Integer mleft;
    private Integer mright;
}
