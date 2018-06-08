package com.littlemonkey.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: xls
 * @Description:
 * @Date: Created in 0:40 2018/3/28
 * @Version: 1.0
 */
public final class CurrentHttpServletHolder {

    /**
     * <p>保存当前线程HttpServletRequest变量副本</p>
     */
    private static ThreadLocal<HttpServletRequest> currentHttpServletRequestThreadLocal;
    /**
     * <p>保存当前线程HttpServletResponse变量副本</p>
     */
    private static ThreadLocal<HttpServletResponse> currentHttpServletResponseThreadLocal;
    /**
     * <p>保存当前线程请求中serviceName变量副本</p>
     */
    private static ThreadLocal<String> currentServerNameThreadLocal;
    /**
     * <p>保存当前线程请求中methodName变量副本</p>
     */
    private static ThreadLocal<String> currentMethodNameThreadLocal;

    static {
        currentHttpServletRequestThreadLocal = new ThreadLocal<>();
        currentHttpServletResponseThreadLocal = new ThreadLocal<>();
        currentServerNameThreadLocal = new ThreadLocal<>();
        currentMethodNameThreadLocal = new ThreadLocal<>();
    }

    public static void setCurrentRequestAndCurrentResponse(HttpServletRequest request, HttpServletResponse response) {
        currentHttpServletRequestThreadLocal.set(request);
        currentHttpServletResponseThreadLocal.set(response);
    }

    public static void setCurrentServerNameAndCurrentMethodName(String serverName, String methodName) {
        currentServerNameThreadLocal.set(serverName);
        currentMethodNameThreadLocal.set(methodName);
    }


    public static HttpServletRequest getCurrentRequest() {
        return currentHttpServletRequestThreadLocal.get();
    }

    public static HttpServletResponse getCurrentResponse() {
        return currentHttpServletResponseThreadLocal.get();
    }

    public static String getCurrentServerName() {
        return currentServerNameThreadLocal.get();
    }

    public static String getCurrentMethodName() {
        return currentMethodNameThreadLocal.get();
    }

    /**
     * <p>清理当前线程变量副本，谨慎使用！</p>
     */
    public static void removeAll() {
        currentServerNameThreadLocal.remove();
        currentMethodNameThreadLocal.remove();
        currentHttpServletRequestThreadLocal.remove();
        currentHttpServletResponseThreadLocal.remove();
    }
}
