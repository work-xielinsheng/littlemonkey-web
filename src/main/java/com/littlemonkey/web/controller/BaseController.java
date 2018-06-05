package com.littlemonkey.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.littlemonkey.utils.reflection.ReflectionUtils2;
import com.littlemonkey.web.annotation.Bind;
import com.littlemonkey.web.context.CurrentHttpServletHolder;
import com.littlemonkey.web.context.SpringContextHolder;
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
import org.springframework.util.Assert;
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
public class BaseController {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * @param body
     */
    protected final void processRequest(RequestMethod requestMethod, RequestBody body) throws Exception {
        Answer answer = new Answer();
        answer.setServiceName(body.getServiceName());
        answer.setMethodName(body.getMethodName());
        logger.info("request body: {}", body);
        try {
            Assert.notNull(answer.getServiceName(), "serviceName is null.");
            Assert.notNull(answer.getMethodName(), "methodName is null.");
            MethodDetail methodDetail = MethodCacheHolder.getTargetMethod(body.getServiceName(), body.getMethodName());
            if (Objects.isNull(methodDetail) || Objects.isNull(methodDetail.getMethod())) {
                throw new NoSuchBeanDefinitionException("");
            }
            RequestDetail requestDetail = new RequestDetail(requestMethod, body, methodDetail);
            Bind bind = body.getClass().getAnnotation(Bind.class);
            MethodBuildProvider methodBuildProvider = SpringContextHolder.getBean((Class<MethodBuildProvider>) bind.target());
            Object[] params = methodBuildProvider.buildParams(requestDetail);
            logger.info("params: {}", Arrays.toString(params));
            Object result = ReflectionUtils2.invokeMethod(SpringContextHolder.getBean(body.getServiceName()), methodDetail.getMethodName(),
                    params, methodDetail.getParameterTypes());
            answer.setResult(result);
        } catch (NoSuchBeanDefinitionException e) {

        } catch (Exception e) {
            throw e;
        }
        this.callBack(answer);
    }

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
