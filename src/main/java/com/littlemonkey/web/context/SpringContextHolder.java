package com.littlemonkey.web.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

@Component
public final class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * <p>get bean by bean name</p>
     *
     * @param beanName
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * <p>get interface all subclass by interface class type</p>
     *
     * @param clazz
     * @return
     */
    public static String[] getBeanNamesForType(Class clazz) {
        return applicationContext.getBeanNamesForType(clazz);
    }

    /**
     * <p>get bean class by bean name</p>
     *
     * @param beanName
     * @return
     */
    public static Class<?> getType(String beanName) {
        return applicationContext.getType(beanName);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
        return applicationContext.getBeanNamesForAnnotation(annotationType);
    }

    /**
     * <p>get bean by bean name</p>
     *
     * @param beanName
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName, Class<? extends Annotation> annotationType) {
        T t = (T) applicationContext.getBean(beanName);
        if (!t.getClass().isAnnotationPresent(annotationType)) {
            throw new NoSuchBeanDefinitionException("Resources don't exist.");
        }
        return t;
    }
}