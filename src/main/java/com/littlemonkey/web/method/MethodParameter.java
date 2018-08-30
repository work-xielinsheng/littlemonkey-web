package com.littlemonkey.web.method;

import com.littlemonkey.utils.base.GenericType;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * @Author: xls
 * @Description:
 * @Date: Created in 16:29 2018/4/4
 * @Version: 1.0
 */
@Data
public class MethodParameter {
    private String parameterName;
    private RequestMethod requestMethod;
    private GenericType genericType;
    private Annotation[] annotations;

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
