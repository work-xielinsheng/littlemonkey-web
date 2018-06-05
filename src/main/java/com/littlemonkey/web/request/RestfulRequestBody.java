package com.littlemonkey.web.request;


import com.littlemonkey.web.annotation.Bind;
import com.littlemonkey.web.method.build.impl.RestfulMethodBuildProviderImpl;

@Bind(target = RestfulMethodBuildProviderImpl.class)
public class RestfulRequestBody implements RequestBody {

    private String serviceName;
    private String methodName;
    private String content;
    private String queryString;
    private long id;

    public RestfulRequestBody(String serviceName, String methodName, String content, String queryString, Long id) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.content = content;
        this.queryString = queryString;
        this.id = id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RestfulRequestBody{" +
                "serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", content='" + content + '\'' +
                ", queryString='" + queryString + '\'' +
                ", id=" + id +
                '}';
    }
}
