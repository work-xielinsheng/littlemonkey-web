package com.littlemonkey.web.controller;

import com.google.common.collect.Lists;
import com.littlemonkey.utils.collect.Collections3;
import com.littlemonkey.utils.lang.JsonUtils;
import com.littlemonkey.utils.lang.Objects2;
import com.littlemonkey.utils.reflection.ReflectionUtils2;
import com.littlemonkey.web.annotation.Bind;
import com.littlemonkey.web.annotation.Interceptor;
import com.littlemonkey.web.annotation.Resources;
import com.littlemonkey.web.commons.ErrorCode;
import com.littlemonkey.web.commons.ValueConstants;
import com.littlemonkey.web.context.CurrentHttpServletHolder;
import com.littlemonkey.web.context.SpringContextHolder;
import com.littlemonkey.web.exception.ApplicationException;
import com.littlemonkey.web.interceptor.MethodInterceptor;
import com.littlemonkey.web.method.MethodCacheHolder;
import com.littlemonkey.web.method.MethodDetail;
import com.littlemonkey.web.method.build.MethodBuildProvider;
import com.littlemonkey.web.param.RequestDetail;
import com.littlemonkey.web.request.RequestBody;
import com.littlemonkey.web.response.Answer;
import com.littlemonkey.web.response.FileResponse;
import com.littlemonkey.web.response.StringResponse;
import com.littlemonkey.web.response.WorkBookResponse;
import com.littlemonkey.web.utils.WebUtils2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author: xls
 * @Description:
 * @Date: Created in 18:01 2018/4/3
 * @Version: 1.0
 */
public abstract class BaseController {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    private ThreadLocal<List<MethodInterceptor>> currentMethodInterceptorThreadLocal = new ThreadLocal<>();

    private void initCurrentMethodInterceptors(Method method) {
        Interceptor interceptor = method.getAnnotation(Interceptor.class);
        Class[] classes = interceptor.value();
        List<MethodInterceptor> methodInterceptors = Lists.newArrayListWithCapacity(classes.length);
        for (Class cls : classes) {
            methodInterceptors.add((MethodInterceptor) SpringContextHolder.getBean(cls));
        }
        currentMethodInterceptorThreadLocal.set(methodInterceptors);
    }

    /**
     * @param body
     */
    public final void processRequest(RequestMethod requestMethod, RequestBody body) throws ApplicationException {
        Answer answer = new Answer();
        answer.setServiceName(body.getServiceName());
        answer.setMethodName(body.getMethodName());
        logger.info("request body: {}", body);
        try {
            CurrentHttpServletHolder.setCurrentServerNameAndCurrentMethodName(body.getServiceName(), body.getMethodName());
            if (!StringUtils.hasText(answer.getServiceName()) || !StringUtils.hasText(answer.getMethodName())) {
                throw new NoSuchBeanDefinitionException("Resources don't exist.");
            }
            MethodDetail methodDetail = MethodCacheHolder.getTargetMethod(answer.getServiceName(), answer.getMethodName());
            if (Objects.isNull(methodDetail) || Objects.isNull(methodDetail.getMethod())) {
                throw new NoSuchBeanDefinitionException("Resources don't exist.");
            }
            // 编译参数
            Bind bind = Objects2.getAnnotation(body, Bind.class);
            MethodBuildProvider methodBuildProvider = SpringContextHolder.getBean((Class<MethodBuildProvider>) bind.target());
            RequestDetail requestDetail = new RequestDetail(requestMethod, body, methodDetail);
            final Object[] params = methodBuildProvider.buildParams(requestDetail);
            logger.info("params: {}", Arrays.toString(params));
            // 初始化对应方法拦截器
            this.initCurrentMethodInterceptors(methodDetail.getMethod());
            // 执行前置方法
            this.before(params);
            final Object result = ReflectionUtils2.invokeMethod(SpringContextHolder.getBean(body.getServiceName(), Resources.class), methodDetail.getMethodName(), params, methodDetail.getParameterTypes());
            // 执行后置方法
            this.after(result);
            // 设置result
            answer.setResult(result);
            answer.setCode(ErrorCode.SC_OK);
            answer.setMessage(ValueConstants.SUCCESS_MESSAGE);
        } catch (NoSuchBeanDefinitionException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ErrorCode.SC_NOT_FOUND, ValueConstants.RESOURCES_NOT_FOUND);
        } catch (ApplicationException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ErrorCode.SC_INTERNAL_SERVER_ERROR, ValueConstants.SERVER_ERROR_MESSAGE);
        } finally {
            currentMethodInterceptorThreadLocal.remove();
        }
        try {
            this.callBack(answer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ErrorCode.SC_INTERNAL_SERVER_ERROR, ValueConstants.SERVER_RESPONSE_ERROR_MESSAGE);
        }
    }

    /**
     * @param params
     * @throws Exception
     */
    private void before(Object[] params) {
        List<MethodInterceptor> methodInterceptors = currentMethodInterceptorThreadLocal.get();
        if (!Collections3.isEmpty(methodInterceptors)) {
            for (MethodInterceptor methodInterceptor : methodInterceptors) {
                methodInterceptor.before(CurrentHttpServletHolder.getCurrentRequest(), params);
            }
        }
    }

    /**
     * @param result
     * @throws Exception
     */
    private void after(Object result) {
        List<MethodInterceptor> methodInterceptors = currentMethodInterceptorThreadLocal.get();
        if (!Collections3.isEmpty(methodInterceptors)) {
            for (MethodInterceptor methodInterceptor : methodInterceptors) {
                methodInterceptor.after(CurrentHttpServletHolder.getCurrentResponse(), result);
            }
        }
    }

    /**
     * <p>回调方法</p>
     *
     * @param answer
     */
    protected void callBack(Answer answer) {
        HttpServletResponse response = CurrentHttpServletHolder.getCurrentResponse();
        if (answer.getResult() instanceof byte[]) {
            WebUtils2.outByte(response, (byte[]) answer.getResult());
        } else if (answer.getResult() instanceof StringResponse) {
            WebUtils2.outText(response, ((StringResponse) answer.getResult()).getStringValue());
        } else if (answer.getResult() instanceof BufferedImage) {
            WebUtils2.outBufferedImage(response, (BufferedImage) answer.getResult());
        } else if (answer.getResult() instanceof WorkBookResponse) {
            WebUtils2.outHSSFWorkbook(response, (WorkBookResponse) answer.getResult());
        } else if (answer.getResult() instanceof FileResponse) {
            WebUtils2.outFile(response, (FileResponse) answer.getResult());
        } else {
            WebUtils2.outJson(response,
                    answer.getResult() instanceof String ?
                            answer.getResult().toString() : JsonUtils.toJSONString(answer));
        }
    }
}
