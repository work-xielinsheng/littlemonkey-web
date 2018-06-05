package com.littlemonkey.web.controller;

import com.littlemonkey.web.annotation.Path;
import com.littlemonkey.web.context.SpringContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebBaseMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] beanNames = SpringContextHolder.getBeanNamesForType(HandlerInterceptor.class);
        for (String beanName : beanNames) {
            HandlerInterceptor handlerInterceptor = SpringContextHolder.getBean(beanName);
            Path path = handlerInterceptor.getClass().getAnnotation(Path.class);
            registry.addInterceptor(handlerInterceptor).addPathPatterns(path.url()).excludePathPatterns(path.exclude());
        }
        super.addInterceptors(registry);
    }
}
