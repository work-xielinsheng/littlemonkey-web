package com.littlemonkey.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface AfterInterceptor {
    void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
