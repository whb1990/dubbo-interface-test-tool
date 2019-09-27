package com.whb.dubbo.service;

/**
 * @author Joey
 * @date 2018/11/26 16:20
 */
public interface MenuService {

    /**
     * get url map to the mid.
     *
     * @param mid menuId
     * @return the menu mrl
     */
    String getUrl(Integer mid);

    /**
     * get the menu text.
     * @return
     */
    String getHtml();
}
