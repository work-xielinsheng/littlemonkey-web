package com.littlemonkey.web.method.resolver;

import com.littlemonkey.web.method.MethodParameter;

import java.lang.reflect.AnnotatedElement;


public interface WebHandlerMethodArgResolver {

    boolean supportsParameter(MethodParameter methodParameter);

    Object resolveArgument(MethodParameter methodParameter, String queryString, String content);
}
