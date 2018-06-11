package com.littlemonkey.web.authority;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Configuration;

/**
 * @author Created by pangkunkun on 2017/11/15.
 */
@Configuration
public class MyShiroRealm extends AuthorizingRealm {

    @Override
    public String getName() {
        return "myShiroRealm";
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        System.out.println("doGetAuthenticationInfo1");
        return null;
    }


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println();
        return null;
    }

}