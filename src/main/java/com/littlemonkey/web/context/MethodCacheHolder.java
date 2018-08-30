package com.littlemonkey.web.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.littlemonkey.utils.collect.Collections3;
import com.littlemonkey.web.annotation.Interceptor;
import com.littlemonkey.web.annotation.Resources;
import com.littlemonkey.web.commons.ValueConstants;
import com.littlemonkey.web.interceptor.MethodInterceptor;
import com.littlemonkey.web.method.MethodDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Auther: xielinsheng
 * @Date: 2018/6/9 00:32
 * @Description:
 */
@Component
@Slf4j
public class MethodCacheHolder {

    @Resource
    private SpringContextHolder springContextHolder;

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

    private static void putMethodDetail(String resourceName, String methodName, MethodDetail methodDetail) {
        putMethodDetail(resourceName + "_" + methodName, methodDetail);
    }

    private static MethodDetail getMethodDetail(String key) {
        return methodDetailCache.get(key);
    }

    public static MethodDetail getMethodDetail(String resourceName, String methodName) {
        return getMethodDetail(resourceName + "_" + methodName);
    }

    public static List<MethodInterceptor> getMethodInterceptor(Method method) {
        return methodInterceptorCache.get(method);
    }

    @PostConstruct
    public void init() {
        String[] resourceNames = springContextHolder.getBeanNamesForAnnotation(Resources.class);
        if (Collections3.isNotEmpty(resourceNames)) {
            for (String resourceName : resourceNames) {
                List<Method> methodList = Lists.newArrayList(SpringContextHolder.getType(resourceName).getDeclaredMethods());
                if (Collections3.isEmpty(methodList)) {
                    continue;
                }
                methodList.forEach((Method method) -> {
                    if (method.getName().contains(ValueConstants.PRIVATE)
                            || method.getName().contains(ValueConstants.STATIC)) {
                        return;
                    }
                    log.info("cache load method name: {}", method.getName());
                    MethodDetail methodDetail = new MethodDetail(method);
                    putMethodDetail(resourceName, method.getName(), methodDetail);
                    Interceptor interceptor = method.getAnnotation(Interceptor.class);
                    if (Objects.nonNull(interceptor)) {
                        Class[] classes = interceptor.value();
                        if (Collections3.isNotEmpty(classes)) {
                            List<MethodInterceptor> methodInterceptors = Lists.newArrayListWithCapacity(classes.length);
                            if (Collections3.isNotEmpty(classes)) {
                                for (Class cls : classes) {
                                    methodInterceptors.add((MethodInterceptor) SpringContextHolder.getBean(cls));
                                }
                            }
                            methodInterceptorCache.put(method, methodInterceptors);
                        }
                    }
                });
            }
        }
    }
}
