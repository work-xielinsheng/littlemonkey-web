package com.littlemonkey.web.test;

import com.littlemonkey.web.annotation.Resources;

/**
 * @Auther: shuHui.xls
 * @Date: 2018/6/10 14:44
 * @Description:
 */
@Resources("test")
public class TestService {

    public void get(long id) {
        System.out.println("????????");
    }
}
