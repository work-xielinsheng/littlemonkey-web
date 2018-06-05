package com.littlemonkey.web.method.build;

import com.google.common.collect.Lists;
import com.littlemonkey.utils.base.GenericType;
import com.littlemonkey.web.context.SpringContextHolder;
import com.littlemonkey.web.method.MethodDetail;
import com.littlemonkey.web.method.MethodParameter;
import com.littlemonkey.web.method.resolver.WebHandlerMethodArgResolver;
import com.littlemonkey.web.param.RequestDetail;
import com.littlemonkey.web.request.RequestBody;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public interface MethodBuildProvider {
    Object[] buildParams(RequestDetail requestDetail);

    default Object[] resolve(RequestDetail requestDetail) {
        MethodDetail methodDetail = requestDetail.getTargetMethodDetail();
        RequestBody body = requestDetail.getRequestBody();
        List<Object> params = Lists.newArrayListWithCapacity(methodDetail.getMethod().getParameterTypes().length);
        String[] beanNames = SpringContextHolder.getBeanNamesForType(WebHandlerMethodArgResolver.class);

        methodDetail.getParamsGenericTypeMap().forEach(new BiConsumer<String, GenericType>() {
            @Override
            public void accept(String paramName, GenericType genericType) {
                Object targetParam = null;
                MethodParameter methodParameter = new MethodParameter();
                methodParameter.setGenericType(genericType);
                methodParameter.setParameterName(paramName);
                methodParameter.setRequestMethod(requestDetail.getRequestMethodType());
                methodParameter.setAnnotations(methodDetail.getParameterAnnotationsMap().get(paramName));
                for (String beanName : beanNames) {
                    WebHandlerMethodArgResolver webHandlerMethodArgResolver = SpringContextHolder.getBean(beanName);
                    if (webHandlerMethodArgResolver.supportsParameter(methodParameter)) {
                        targetParam = webHandlerMethodArgResolver.resolveArgument(methodParameter, body.getQueryString(), body.getContent());
                        break;
                    }
                }
                params.add(targetParam);
            }
        });
        return params.toArray();
    }
}
