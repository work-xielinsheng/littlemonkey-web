package com.littlemonkey.web.authority;

import com.google.common.collect.Maps;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

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
        Map<String, String> userMaps = Maps.newHashMap();
        userMaps.put("username", "xielinsheng");
        userMaps.put("password", "123456");
        return new SimpleAuthenticationInfo(
                userMaps,
                userMaps.get("password"),
                getName()
        );
    }


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("88888888888888888888");
        return null;
    }

}