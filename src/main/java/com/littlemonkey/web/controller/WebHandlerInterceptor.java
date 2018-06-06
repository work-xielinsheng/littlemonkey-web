package com.littlemonkey.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.littlemonkey.utils.collect.Collections3;
import com.littlemonkey.web.annotation.Path;
import com.littlemonkey.web.comparator.IndexComparator;
import com.littlemonkey.web.context.CurrentHttpServletHolder;
import com.littlemonkey.web.context.SpringContextHolder;
import com.littlemonkey.web.interceptor.AfterInterceptor;
import com.littlemonkey.web.interceptor.BeforeInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
@Path
public class WebHandlerInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(WebHandlerInterceptor.class);

    private BeforeInterceptor[] beforeInterceptors;
    private AfterInterceptor[] afterInterceptors;

    @PostConstruct
    public void initInterceptor() {
        logger.info("init Interceptor start... ");
        Map<String, BeforeInterceptor> beforeInterceptorMap = SpringContextHolder.getBeansOfType(BeforeInterceptor.class);
        if (Collections3.isNotEmpty(beforeInterceptorMap)) {
            Comparator<BeforeInterceptor> beforeInterceptorComparator = Ordering.from(new IndexComparator());
            List<BeforeInterceptor> beforeInterceptorList = Lists.newArrayList(beforeInterceptorMap.values());
            Collections.sort(beforeInterceptorList, beforeInterceptorComparator);
            this.beforeInterceptors = (BeforeInterceptor[]) beforeInterceptorList.toArray();
        }
        Map<String, AfterInterceptor> afterInterceptorMap = SpringContextHolder.getBeansOfType(AfterInterceptor.class);
        if (Collections3.isNotEmpty(afterInterceptorMap)) {
            Comparator<AfterInterceptor> afterInterceptorComparator = Ordering.from(new IndexComparator());
            List<AfterInterceptor> afterInterceptorList = Lists.newArrayList(afterInterceptorMap.values());
            Collections.sort(afterInterceptorList, afterInterceptorComparator);
            this.afterInterceptors = (AfterInterceptor[]) afterInterceptorList.toArray();
        }
        logger.info("init Interceptor stop...");
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (Collections3.isEmpty(beforeInterceptors)) {
            return true;
        }
        for (BeforeInterceptor beforeInterceptor : beforeInterceptors) {
            if (!beforeInterceptor.preHandle(httpServletRequest, httpServletResponse)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        if (!Collections3.isEmpty(afterInterceptors)) {
            for (AfterInterceptor afterInterceptor : afterInterceptors) {
                afterInterceptor.afterCompletion(httpServletRequest, httpServletResponse);
            }
        }
        CurrentHttpServletHolder.removeAll();
    }
}
