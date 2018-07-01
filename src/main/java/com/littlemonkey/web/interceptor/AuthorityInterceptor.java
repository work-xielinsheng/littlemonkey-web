package com.littlemonkey.web.interceptor;

import com.littlemonkey.web.exception.UnauthorizedException;

/**
 * @Author shuHui.xls
 * @Date 2018/7/1 下午9:46
 * @Version 1.0
 * @Description
 */
public interface AuthorityInterceptor {

    void interceptor(Object target) throws UnauthorizedException;

}
