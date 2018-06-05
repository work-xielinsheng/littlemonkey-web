package com.littlemonkey.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: xls
 * @Description:
 * @Date: Created in 0:40 2018/3/28
 * @Version: 1.0
 */
public class CurrentHttpServletHolder {

    private static ThreadLocal<HttpServletRequest> currentHttpServletRequestThreadLocal;
    private static ThreadLocal<HttpServletResponse> currentHttpServletResponseThreadLocal;

    static {
        currentHttpServletRequestThreadLocal = new ThreadLocal<>();
        currentHttpServletResponseThreadLocal = new ThreadLocal<>();
    }

    public static void setCurrentRequestAndCurrentResponse(HttpServletRequest request, HttpServletResponse response) {
        currentHttpServletRequestThreadLocal.set(request);
        currentHttpServletResponseThreadLocal.set(response);
    }

    public static HttpServletRequest getCurrentRequest() {
        return currentHttpServletRequestThreadLocal.get();
    }

    public static HttpServletResponse getCurrentResponse() {
        return currentHttpServletResponseThreadLocal.get();
    }

}
