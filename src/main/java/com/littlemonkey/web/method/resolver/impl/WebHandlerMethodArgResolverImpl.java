package com.littlemonkey.web.method.resolver.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.littlemonkey.utils.base.GenericType;
import com.littlemonkey.utils.collect.Collections3;
import com.littlemonkey.utils.lang.StringUtils;
import com.littlemonkey.web.method.MethodParameter;
import com.littlemonkey.web.method.resolver.WebHandlerMethodArgResolver;
import com.littlemonkey.web.utils.NumberUtils;
import com.littlemonkey.web.utils.Objects2;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WebHandlerMethodArgResolverImpl implements WebHandlerMethodArgResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Collections3.isEmpty(methodParameter.getAnnotations());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, String queryString, String content) {
        final GenericType genericType = methodParameter.getGenericType();
        final String parameterName = methodParameter.getParameterName();
        final Class tClass = genericType.getOwnerType();
        if (RequestMethod.GET.equals(methodParameter.getRequestMethod()) || RequestMethod.DELETE.equals(methodParameter.getRequestMethod())) {
            if (StringUtils.isNotBlank(queryString)) {
                List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(queryString, StandardCharsets.UTF_8);
                Map<String, String> queryBody = nameValuePairs.stream().collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
                String value = queryBody.get(parameterName);
                if (Objects2.isSimple(tClass)) {
                    if (NumberUtils.isNumberClass(tClass)) {
                        return NumberUtils.parseNumber(value, tClass);
                    } else {
                        if (boolean.class.equals(tClass) || Boolean.class.equals(tClass)) {
                            return Boolean.parseBoolean(value);
                        } else if (char.class.equals(tClass) || Character.class.equals(tClass)) {
                            return value.toCharArray()[0];
                        } else if (String.class.equals(tClass)) {
                            return value;
                        }
                    }
                } else {
                    String jsonString = JSONObject.toJSONString(queryBody);
                    return JSONObject.parseObject(jsonString, tClass);
                }
            }
        } else if (RequestMethod.POST.equals(methodParameter.getRequestMethod()) || RequestMethod.PUT.equals(methodParameter.getRequestMethod())) {
            if (StringUtils.isNotBlank(content)) {
                Object contentBody = JSON.parse(content);
                if (Collections3.isContainer(tClass)) {// 方法参数是个Collection 或者Array类型,将content内容映射
                    JSONArray jsonArray = (JSONArray) contentBody;
                    if (Collections3.isEmpty(jsonArray)) {
                        return null;
                    }
                    GenericType targetType = genericType.getGenericTypeList().get(0);//获取list泛型类型
                    List list = jsonArray.toJavaList(targetType.getOwnerType());
                    if (tClass.isArray()) {// 是个array
                        return list.toArray();
                    } else {// 是个Collection
                        return list;
                    }
                } else {// 否则是个Map 或者 Bean
                    JSONObject jsonObject = (JSONObject) contentBody;
                    if (Collections3.isEmpty(jsonObject)) {
                        return null;
                    }
                    if (Collections3.isMap(tClass)) {// 参数是个map
                        return jsonObject.getInnerMap();
                    } else {// 参数是个bean
                        return jsonObject.toJavaObject(tClass);
                    }
                }
            }
        }
        return null;
    }
}
