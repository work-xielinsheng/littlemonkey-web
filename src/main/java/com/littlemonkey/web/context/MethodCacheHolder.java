package com.littlemonkey.web.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.littlemonkey.utils.collect.Collections3;
import com.littlemonkey.web.annotation.Interceptor;
import com.littlemonkey.web.annotation.Resources;
import com.littlemonkey.web.interceptor.MethodInterceptor;
import com.littlemonkey.web.method.MethodDetail;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Auther: xielinsheng
 * @Date: 2018/6/9 00:32
 * @Description:
 */
public class MethodCacheHolder {
    /**
     * 缓存方法的拦截器
     */
    private static final Map<Method, List<MethodInterceptor>> methodInterceptorCache = Maps.newHashMap();

    /**
     * 缓存方法的基本信息
     */
    private static final Map<String, MethodDetail> methodDetailCache = Maps.newHashMap();

    private static void putMethodDetail(String key, MethodDetail methodDetail) {
        methodDetailCache.put(key, methodDetail);
    }

    private static MethodDetail getMethodDetail(String key) {
        return methodDetailCache.get(key);
    }

    private static void putMethodDetail(String resourceName, String methodName, MethodDetail methodDetail) {
        putMethodDetail(resourceName + "_" + methodName, methodDetail);
    }

    @PostConstruct
    public void init() {
        String[] resourceNames = SpringContextHolder.getBeanNamesForAnnotation(Resources.class);
        for (String resourceName : resourceNames) {
            List<Method> methodList = Lists.newArrayList(SpringContextHolder.getType(resourceName).getMethods());
            methodList.forEach((Method method) -> {
                MethodDetail methodDetail = new MethodDetail(method);
                putMethodDetail(resourceName, method.getName(), methodDetail);
                Interceptor interceptor = method.getAnnotation(Interceptor.class);
                Class[] classes = interceptor.value();
                List<MethodInterceptor> methodInterceptors = Lists.newArrayListWithCapacity(classes.length);
                if (Collections3.isNotEmpty(classes)) {
                    for (Class cls : classes) {
                        methodInterceptors.add((MethodInterceptor) SpringContextHolder.getBean(cls));
                    }
                }
                methodInterceptorCache.put(method, methodInterceptors);
            });
        }
    }

    public static MethodDetail getMethodDetail(String resourceName, String methodName) {
        return getMethodDetail(resourceName + "_" + methodName);
    }

    public static List<MethodInterceptor> getMethodInterceptor(Method method) {
        return methodInterceptorCache.get(method);
    }
}
