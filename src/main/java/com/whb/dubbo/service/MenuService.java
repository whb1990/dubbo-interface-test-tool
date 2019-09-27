package com.whb.dubbo.service;

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
     *
     * @return
     */
    String getHtml();
}
