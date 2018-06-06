package com.littlemonkey.web.test;

import com.littlemonkey.web.commons.ErrorCode;
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
    @Override
    public void before(HttpServletRequest request, Object... params) throws ApplicationException {
        long id = (long) params[0];
        if (id < 1000) {
            throw new ApplicationException(ErrorCode.SC_GONE, "id max lt 1000");
        }
    }

    @Override
    public void after(HttpServletResponse response, Object result) throws ApplicationException {

    }
}
