package com.littlemonkey.web.method;

import com.littlemonkey.utils.base.GenericType;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @Author: xls
 * @Description:
 * @Date: Created in 16:29 2018/4/4
 * @Version: 1.0
 */
public class MethodParameter {
    private String parameterName;
    private RequestMethod requestMethod;
    private GenericType genericType;
    private Annotation[] annotations;

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public GenericType getGenericType() {
        return genericType;
    }

    public void setGenericType(GenericType genericType) {
        this.genericType = genericType;
    }


    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    @Override
    public String toString() {
        return "MethodParameter{" +
                "parameterName='" + parameterName + '\'' +
                ", requestMethod=" + requestMethod +
                ", genericType=" + genericType +
                ", annotations=" + Arrays.toString(annotations) +
                '}';
    }
}
