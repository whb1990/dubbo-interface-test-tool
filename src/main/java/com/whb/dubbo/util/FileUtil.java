package com.whb.dubbo.util;

import com.whb.dubbo.exception.RRException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 文件工具类
 */
@Slf4j
public class FileUtil {
    /**
     * 读取文件内容
     *
     * @param fileName
     * @return
     * @throws RRException
     */
    public static String readToString(String fileName) throws RRException {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            int read = in.read(fileContent);
            in.close();
            log.info("read:{} fileLength:{}", read, fileLength);
        } catch (IOException e) {
            throw new RRException(StringUtil.format("无法读取文件内容,原因 {}.", e.getMessage()));
        }
        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RRException(StringUtil.format("无法读取文件内容, 原因 {}.", e.getMessage()));
        }
    }

    /**
     * 内容写出到文件
     *
     * @param fileName 文件名
     * @param text     文件内容
     */
    public static void WriteStringToFile(String fileName, String text) {
        try {
            try (PrintWriter out = new PrintWriter(new File(fileName).getAbsoluteFile())) {
                out.print(text);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
