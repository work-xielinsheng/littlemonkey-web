package com.littlemonkey.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.littlemonkey.utils.reflection.ReflectionUtils2;
import com.littlemonkey.web.annotation.Bind;
import com.littlemonkey.web.annotation.Resources;
import com.littlemonkey.web.common.ErrorCode;
import com.littlemonkey.web.context.CurrentHttpServletHolder;
import com.littlemonkey.web.context.SpringContextHolder;
import com.littlemonkey.web.exception.ApplicationException;
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
import java.util.Arrays;
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
    protected final void processRequest(RequestMethod requestMethod, RequestBody body) throws ApplicationException {
        Answer answer = new Answer();
        answer.setServiceName(body.getServiceName());
        answer.setMethodName(body.getMethodName());
        CurrentHttpServletHolder.setCurrentServerNameAndCurrentMethodName(body.getServiceName(), body.getMethodName());
        logger.info("request body: {}", body);
        try {
            if (!StringUtils.hasText(answer.getServiceName()) || !StringUtils.hasText(answer.getMethodName())) {
                throw new NoSuchBeanDefinitionException("Resources don't exist.");
            }
            MethodDetail methodDetail = MethodCacheHolder.getTargetMethod(answer.getServiceName(), answer.getMethodName());
            if (Objects.isNull(methodDetail) || Objects.isNull(methodDetail.getMethod())) {
                throw new NoSuchBeanDefinitionException("Resources don't exist.");
            }
            Bind bind = body.getClass().getAnnotation(Bind.class);
            MethodBuildProvider methodBuildProvider = SpringContextHolder.getBean((Class<MethodBuildProvider>) bind.target());

            RequestDetail requestDetail = new RequestDetail(requestMethod, body, methodDetail);
            Object[] params = methodBuildProvider.buildParams(requestDetail);
            logger.info("params: {}", Arrays.toString(params));
            Object result = ReflectionUtils2.invokeMethod(SpringContextHolder.getBean(body.getServiceName(), Resources.class),
                    methodDetail.getMethodName(), params, methodDetail.getParameterTypes());
            answer.setResult(result);
        } catch (NoSuchBeanDefinitionException e) {
            throw new ApplicationException(ErrorCode.SC_NOT_FOUND, "Resources don't exist.");
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SC_INTERNAL_SERVER_ERROR, "Internal server error.");
        }
        try {
            this.callBack(answer);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.SC_INTERNAL_SERVER_ERROR, "Server response error.");
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
                            answer.getResult().toString() : JSONObject.toJSONString(answer));
        }
    }
}
