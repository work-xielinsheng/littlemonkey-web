package com.littlemonkey.web.authority;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class WebXX implements  WebAuthorizingUserInfo {
    @Override
    public String getPassword(String username) {
        return "1232444";
    }

    @Override
    public Set<String> getRoles(String username) {
        Set<String> strings = Sets.newHashSet();
        strings.add("admin");
        return strings;
    }

    @Override
    public Set<String> getPermissionsByRole(String role) {
        Set<String> strings = Sets.newHashSet();
        strings.add("add");
        return strings;
    }
}
