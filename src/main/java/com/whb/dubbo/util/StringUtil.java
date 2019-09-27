package com.whb.dubbo.util;

/**
 * 字符串工具类.
 */
public class StringUtil {

    /**
     * 简单格式化{}样式的字符串.<br>
     *
     * @param src   源字符串
     * @param param 跟源字符串{}匹配的个数字符串
     * @return
     */
    public static String format(String src, Object... param) {
        int i = 0;
        int index = 0;
        StringBuffer sb = new StringBuffer(src);
        while (-1 != (index = sb.indexOf("{}"))) {
            sb.replace(index, index + 2, String.valueOf(param[i++]));
        }
        return sb.toString();
    }

    public static String jsonJoin(String[] array) {
        int arraySize = array.length;
        int bufSize = arraySize * (array[0].length() + 3);
        StringBuilder buf = new StringBuilder(bufSize);
        for (int i = 0; i < arraySize; ++i) {
            if (i > 0) {
                buf.append(',');
            }
            buf.append('"');
            buf.append(array[i]);
            buf.append('"');
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        String str = "aaa{} bbb{} ccc{}";
        System.out.println(StringUtil.format(str, "1", "2", "3"));
    }
}
