package com.littlemonkey.web.utils;

public final class NumberUtils {

    /**
     * <p>判断类class是否是number class</p>
     *
     * @param targetClass
     * @return
     */
    public static boolean isNumberClass(Class targetClass) {
        return int.class.equals(targetClass) || byte.class.equals(targetClass) || double.class.equals(targetClass) || short.class.equals(targetClass) || long.class.equals(targetClass) || float.class.equals(targetClass) ||
                Integer.class.equals(targetClass) || Double.class.equals(targetClass) || Byte.class.equals(targetClass) || Short.class.equals(targetClass) || Long.class.equals(targetClass) || Float.class.equals(targetClass);
    }

    /**
     * <p>将字符串转换成对应number类型数据</p>
     *
     * @param text        需要转换的字符串
     * @param targetClass 目标类型,支持基本类型和包装类
     * @param <T>
     * @return
     */
    public static <T> T parseNumber(String text, Class targetClass) {
        Class tClass = targetClass;
        if (int.class.equals(targetClass)) {
            tClass = Integer.class;
        } else if (byte.class.equals(targetClass)) {
            tClass = Byte.class;
        } else if (short.class.equals(targetClass)) {
            tClass = Short.class;
        } else if (long.class.equals(targetClass)) {
            tClass = Long.class;
        } else if (float.class.equals(targetClass)) {
            tClass = Float.class;
        }
        return (T) org.springframework.util.NumberUtils.parseNumber(text, tClass);
    }
}
