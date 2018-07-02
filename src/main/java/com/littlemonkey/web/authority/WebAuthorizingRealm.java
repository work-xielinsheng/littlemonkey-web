package com.littlemonkey.web.authority;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class WebAuthorizingRealm extends AuthorizingRealm {

    private final static Logger logger = LoggerFactory.getLogger(WebAuthorizingRealm.class);

    @Autowired(required = false)
    private WebAuthorizingUserInfo webAuthorizingUserInfo;

    /**
     * <p>授权</p>
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("execute doGetAuthorizationInfo...");
        String username = (String) super.getAvailablePrincipal(principalCollection);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> roles = webAuthorizingUserInfo.getRoles(username);
        authorizationInfo.setRoles(roles);
        authorizationInfo.addStringPermissions(webAuthorizingUserInfo.getPermissionsByRoles(roles));
        return authorizationInfo;
    }


    /**
     * <p>认证</p>
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("execute doGetAuthenticationInfo...");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = webAuthorizingUserInfo.getPassword(username);
        logger.info("username: {}, password: {}", username, password);
        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
