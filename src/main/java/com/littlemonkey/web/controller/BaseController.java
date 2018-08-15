package com.littlemonkey.web.controller;

import com.littlemonkey.utils.collection.Collections3;
import com.littlemonkey.utils.lang.JsonUtils;
import com.littlemonkey.utils.lang.Objects2;
import com.littlemonkey.utils.reflection.ReflectionUtils2;
import com.littlemonkey.web.annotation.Bind;
import com.littlemonkey.web.annotation.Resources;
import com.littlemonkey.web.commons.ErrorCode;
import com.littlemonkey.web.commons.ValueConstants;
import com.littlemonkey.web.context.CurrentHttpServletHolder;
import com.littlemonkey.web.context.MethodCacheHolder;
import com.littlemonkey.web.context.SpringContextHolder;
import com.littlemonkey.web.exception.ApplicationException;
import com.littlemonkey.web.interceptor.MethodInterceptor;
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

import javax.servlet.http.HttpServletRequest;
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

    /**
     * @param body
     */
    public final void processRequest(final RequestMethod requestMethod, final RequestBody body) throws ApplicationException {
        final Answer answer = new Answer();
        answer.setServiceName(body.getServiceName());
        answer.setMethodName(body.getMethodName());
        logger.info("request body: {}", body);
        HttpServletRequest request = CurrentHttpServletHolder.getCurrentRequest();
        HttpServletResponse response = CurrentHttpServletHolder.getCurrentResponse();
        try {
            CurrentHttpServletHolder.setCurrentServerNameAndCurrentMethodName(body.getServiceName(), body.getMethodName());
            if (!StringUtils.hasText(answer.getServiceName()) || !StringUtils.hasText(answer.getMethodName())) {
                throw new NoSuchBeanDefinitionException(ValueConstants.RESOURCES_NOT_FOUND);
            }
            final MethodDetail methodDetail = MethodCacheHolder.getMethodDetail(answer.getServiceName(), answer.getMethodName());
            if (Objects.isNull(methodDetail)) {
                throw new NoSuchBeanDefinitionException(ValueConstants.RESOURCES_NOT_FOUND);
            }
            final Method targetMethod = methodDetail.getMethod();
            if (Objects.isNull(targetMethod)) {
                throw new NoSuchBeanDefinitionException(ValueConstants.RESOURCES_NOT_FOUND);
            }
            // 权限验证

            // 编译参数
            Bind bind = Objects2.getAnnotation(body, Bind.class);
            MethodBuildProvider methodBuildProvider = SpringContextHolder.getBean((Class<MethodBuildProvider>) bind.target());
            RequestDetail requestDetail = new RequestDetail(requestMethod, body, methodDetail);
            final Object[] params = methodBuildProvider.buildParams(requestDetail);
            logger.info("params: {}", Arrays.toString(params));
            // 执行前置方法
            this.before(request, targetMethod, params);
            final Object result = ReflectionUtils2.invokeMethod(SpringContextHolder.getBean(body.getServiceName(), Resources.class), methodDetail.getMethodName(), params, methodDetail.getParameterTypes());
            // 执行后置方法
            this.after(response, targetMethod, result);
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
        }
        try {
            this.callBack(response, answer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ErrorCode.SC_INTERNAL_SERVER_ERROR, ValueConstants.SERVER_RESPONSE_ERROR_MESSAGE);
        }
    }

    /**
     * @param params
     * @throws Exception
     */
    private void before(HttpServletRequest request, Method method, Object[] params) {
        List<MethodInterceptor> methodInterceptors = MethodCacheHolder.getMethodInterceptor(method);
        if (Collections3.isNotEmpty(methodInterceptors)) {
            methodInterceptors.forEach((MethodInterceptor methodInterceptor) -> {
                methodInterceptor.before(request, params);
            });
        }
    }

    /**
     * @param result
     * @throws Exception
     */
    private void after(HttpServletResponse response, Method method, Object result) {
        List<MethodInterceptor> methodInterceptors = MethodCacheHolder.getMethodInterceptor(method);
        if (Collections3.isNotEmpty(methodInterceptors)) {
            methodInterceptors.forEach((MethodInterceptor methodInterceptor) -> {
                methodInterceptor.after(response, result);
            });
        }
    }

    /**
     * <p>回调方法</p>
     */
    protected void callBack(HttpServletResponse response, Answer answer) {
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
