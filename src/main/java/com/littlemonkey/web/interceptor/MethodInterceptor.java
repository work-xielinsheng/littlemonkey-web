package com.littlemonkey.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MethodInterceptor {

    void before(HttpServletRequest request, Object... params) throws Exception;

    void after(HttpServletResponse response,Object result) throws Exception;
}
