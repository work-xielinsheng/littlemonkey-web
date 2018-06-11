package com.littlemonkey.web.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Created by xielinsheng on 2017/11/18.
 */
public class WebAccessControlFilter extends AccessControlFilter {

    private static final Logger log = LoggerFactory.getLogger(WebAccessControlFilter.class);


    /**
     * 表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，如果允许访问返回true，否则false；
     * (感觉这里应该是对白名单（不需要登录的接口）放行的)
     * 如果isAccessAllowed返回true则onAccessDenied方法不会继续执行
     * 这里可以用来判断一些不被通过的链接（个人备注）
     * * 表示是否允许访问 ，如果允许访问返回true，否则false；
     *
     * @param servletRequest
     * @param servletResponse
     * @param object          表示写在拦截器中括号里面的字符串 mappedValue 就是 [urls] 配置中拦截器参数部分
     * @return
     * @throws Exception
     */
    @Override
    public boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object object) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        String url = getPathWithinApplication(servletRequest);
        log.info("当前用户正在访问的 url => " + url);
        log.info("subject.isPermitted(url);" + subject.isPermitted(url));
        return false;
    }

    /**
     * 表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
     * onAccessDenied是否执行取决于isAccessAllowed的值，如果返回true则onAccessDenied不会执行；如果返回false，执行onAccessDenied
     * 如果onAccessDenied也返回false，则直接返回，不会进入请求的方法（只有isAccessAllowed和onAccessDenied的情况下）
     */
    @Override
    public boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        try {
            System.out.println();
        } catch (Exception e) {
            log.info("登陆失败");
            log.info(e.getMessage());
            return false;
        }
        log.info("登陆成功");
        return true;
    }

}
