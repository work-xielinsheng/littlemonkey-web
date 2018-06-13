package com.littlemonkey.web.test;

import com.littlemonkey.web.annotation.Resources;

/**
 * @Auther: xielinsheng
 * @Date: 2018/6/10 14:44
 * @Description:
 */
@Resources("test")
public class TestService {

    public void get(long id) {
        Subject subject = SecurityUtils.getSubject();
        System.out.println(id);
    }
}
