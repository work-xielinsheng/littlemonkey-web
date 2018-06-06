package com.littlemonkey.web.test;

import com.littlemonkey.web.annotation.Interceptor;
import com.littlemonkey.web.annotation.Resources;

@Resources("test")
public class TestService {

    @Interceptor(TestMethodInterceptor.class)
    public String get(long id) {
        return String.valueOf(id);
    }
}
