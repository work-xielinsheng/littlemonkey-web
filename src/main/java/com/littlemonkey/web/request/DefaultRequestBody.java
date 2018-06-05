package com.littlemonkey.web.request;

import com.littlemonkey.web.annotation.Bind;
import com.littlemonkey.web.method.build.impl.DefaultMethodBuildProviderImpl;

/**
 * @Author: xls
 * @Description:
 * @Date: Created in 20:32 2018/4/3
 * @Version: 1.0
 */
@Bind(target = DefaultMethodBuildProviderImpl.class)
public class DefaultRequestBody implements RequestBody {

    private String serviceName;
    private String methodName;
    private String content;
    private String queryString;

    public DefaultRequestBody(String serviceName, String methodName, String content, String queryString) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.content = content;
        this.queryString = queryString;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Override
    public String toString() {
        return "DefaultRequestBody{" +
                "serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", content='" + content + '\'' +
                ", queryString='" + queryString + '\'' +
                '}';
    }
}
