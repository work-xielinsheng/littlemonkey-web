package com.littlemonkey.web.interceptor;

import com.littlemonkey.web.exception.ApplicationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MethodInterceptor {

    void before(HttpServletRequest request, Object... params) throws ApplicationException;

    void after(HttpServletResponse response,Object result) throws ApplicationException;
}
