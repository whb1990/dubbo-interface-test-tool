/*
 * Copyright (c) 2010-2020 Founder Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Founder. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the agreements
 * you entered into with Founder.
 *
 */
package com.whb.dubbo.test;

import com.whb.dubbo.context.DubboClassLoader;
import org.junit.Test;

/**
 * @author Joey
 * @date 2019/6/28 14:54
 */
public class TestDubboClassLoader {

    @Test
    public void testLoad() throws Exception {

        String path = "F:\\app\\dubbo\\lib";

        DubboClassLoader dubboClassLoader = new DubboClassLoader(path);

        dubboClassLoader.loadJars();
        dubboClassLoader.loadClassFile();
        Class<?> clazz = dubboClassLoader.loadClass("com.fcbox.edms.terminal.api.CabinetServiceFacade");


        System.out.println(clazz.getClassLoader());

    }

}
