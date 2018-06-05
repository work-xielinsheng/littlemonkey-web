package com.littlemonkey.web.method;

import com.google.common.collect.Maps;
import com.littlemonkey.utils.base.GenericType;
import com.littlemonkey.utils.reflection.ReflectionUtils2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xls
 * @Description: packaging method detail
 * @Date: Created in 16:02 2018/4/4
 * @Version: 1.0
 */
public class MethodDetail {

    /**
     * <p>target method</p>
     */
    private Method method;

    private String[] paramNames;

    /**
     * <p>method params generic detail</p>
     * <p>LinkedHashMap ensure param order,map key is param name,map vue is param generic detail</p>
     */
    private LinkedHashMap<String, GenericType> paramsGenericTypeMap;

    private LinkedHashMap<String, Annotation[]> parameterAnnotationsMap;


    public MethodDetail(Method method) {
        this.method = method;
        this.paramNames = ReflectionUtils2.getParameterNames(this.method);
        Map<String, GenericType> paramsGenericTypeMap = Maps.newLinkedHashMap();
        Map<String, Annotation[]> parameterAnnotationsMap = Maps.newLinkedHashMap();
        Annotation[][] annotations = this.method.getParameterAnnotations();
        List<GenericType> genericTypeList = ReflectionUtils2.getGenericType(method);
        for (int i = 0; i < paramNames.length; i++) {
            paramsGenericTypeMap.put(paramNames[i], genericTypeList.get(i));
            parameterAnnotationsMap.put(paramNames[i], annotations[i]);
        }
        this.setParameterAnnotationsMap((LinkedHashMap<String, Annotation[]>) parameterAnnotationsMap);
        this.setParamsGenericTypeMap((LinkedHashMap<String, GenericType>) paramsGenericTypeMap);
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public LinkedHashMap<String, GenericType> getParamsGenericTypeMap() {
        return paramsGenericTypeMap;
    }

    public void setParamsGenericTypeMap(LinkedHashMap<String, GenericType> paramsGenericTypeMap) {
        this.paramsGenericTypeMap = paramsGenericTypeMap;
    }

    public LinkedHashMap<String, Annotation[]> getParameterAnnotationsMap() {
        return parameterAnnotationsMap;
    }

    public void setParameterAnnotationsMap(LinkedHashMap<String, Annotation[]> parameterAnnotationsMap) {
        this.parameterAnnotationsMap = parameterAnnotationsMap;
    }

    public Class[] getParameterTypes() {
        return this.method.getParameterTypes();
    }

    public String getMethodName() {
        return this.method.getName();
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public void setParamNames(String[] paramNames) {
        this.paramNames = paramNames;
    }

    @Override
    public String toString() {
        return "MethodDetail{" +
                "method=" + method +
                ", paramsGenericTypeMap=" + paramsGenericTypeMap +
                ", parameterAnnotationsMap=" + parameterAnnotationsMap +
                '}';
    }
}


