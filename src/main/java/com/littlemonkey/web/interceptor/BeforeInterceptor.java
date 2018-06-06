package com.littlemonkey.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BeforeInterceptor {

    boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
