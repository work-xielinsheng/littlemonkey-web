package com.littlemonkey.web.test;

import com.littlemonkey.web.annotation.Resources;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 * @Auther: shuHui.xls
 * @Date: 2018/6/10 14:44
 * @Description:
 */
@Resources("test")
public class TestService {

    public void get(long id) {
        Subject subject = SecurityUtils.getSubject();
        System.out.println(subject.isAuthenticated());
        System.out.println(subject.getPrincipal());
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("xielinsheng", "1232444");
        usernamePasswordToken.setRememberMe(true);
        subject.login(usernamePasswordToken);
        System.out.println(subject.isPermitted("add"));
        System.out.println(subject.isAuthenticated());
        System.out.println(subject.isRemembered());
    }
}
