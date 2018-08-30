package com.littlemonkey.web.utils;

import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

public class Objects2 {

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean nonNull(Object obj) {
        return obj != null;
    }

    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }

    /**
     * <p>获取对象的注解实体</p>
     *
     * @param target
     * @param annotationClass
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A getAnnotation(Object target, Class<A> annotationClass) {
        Objects2.requireNonNull(target);
        return target.getClass().getAnnotation(annotationClass);
    }

    /**
     * <p>判断是否是包装类或者基本类型以及String</p>
     *
     * @param object
     * @return
     */
    public static boolean isSimple(Object object) {
        Objects2.requireNonNull(object);
        Class tClass = object.getClass();
        try {
            return StringUtils.endsWithIgnoreCase(tClass.getTypeName(), String.class.getTypeName())
                    || ((Class) tClass.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return tClass.isPrimitive();
        }
    }

    public static void main(String[] args) {

    }
}
