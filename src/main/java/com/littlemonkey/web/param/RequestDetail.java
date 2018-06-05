package com.littlemonkey.web.param;

import com.littlemonkey.web.method.MethodDetail;
import com.littlemonkey.web.request.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

public class RequestDetail {

    private RequestMethod requestMethodType;

    private RequestBody requestBody;

    private MethodDetail targetMethodDetail;

    public RequestDetail(RequestMethod requestMethodType, RequestBody requestBody, MethodDetail targetMethodDetail) {
        this.requestMethodType = requestMethodType;
        this.requestBody = requestBody;
        this.targetMethodDetail = targetMethodDetail;
    }

    public RequestMethod getRequestMethodType() {
        return requestMethodType;
    }

    public void setRequestMethodType(RequestMethod requestMethodType) {
        this.requestMethodType = requestMethodType;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public MethodDetail getTargetMethodDetail() {
        return targetMethodDetail;
    }

    public void setTargetMethodDetail(MethodDetail targetMethodDetail) {
        this.targetMethodDetail = targetMethodDetail;
    }
}
