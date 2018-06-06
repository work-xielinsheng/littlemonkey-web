package com.littlemonkey.web.test;

import com.littlemonkey.web.exception.ApplicationException;
import com.littlemonkey.web.interceptor.MethodInterceptor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@Component
public class TestMethodInterceptor implements MethodInterceptor {

    private Long a;

    @Override
    public void before(HttpServletRequest request, Object... params) throws ApplicationException {

    }

    @Override
    public void after(HttpServletResponse response, Object result) throws ApplicationException {

    }
}
