package com.littlemonkey.web.authority;

import java.util.Set;

public interface WebAuthorizingUserInfo {

    String getPassword(String username);

    Set<String> getRoles(String username);

    Set<String> getPermissionsByRoles(Set<String> roles);
}
