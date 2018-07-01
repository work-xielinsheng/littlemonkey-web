package com.littlemonkey.web.authority;

import com.google.common.collect.Lists;
import com.littlemonkey.utils.lang.Objects2;
import com.littlemonkey.web.annotation.RequiresAuthentication;
import com.littlemonkey.web.annotation.RequiresPermissions;
import com.littlemonkey.web.annotation.RequiresRoles;
import com.littlemonkey.web.exception.UnauthorizedException;
import com.littlemonkey.web.interceptor.AuthorityInterceptor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @Author shuHui.xls
 * @Date 2018/7/1 下午9:49
 * @Version 1.0
 * @Description
 */
@Component
public class DefaultAuthorityInterceptor implements AuthorityInterceptor {
    @Override
    public void interceptor(Object target) throws UnauthorizedException {
        try {
            Objects2.requireNonNull(target);
            RequiresRoles requiresRoles = target.getClass().getAnnotation(RequiresRoles.class);
            RequiresPermissions requiresPermissions = target.getClass().getAnnotation(RequiresPermissions.class);
            RequiresAuthentication requiresAuthentication = target.getClass().getAnnotation(RequiresAuthentication.class);
            Subject subject = SecurityUtils.getSubject();
            if (Objects2.nonNull(requiresAuthentication)) {
                subject.isAuthenticated();
            }
            if (Objects2.nonNull(requiresRoles)) {
                List<String> roles = Lists.newArrayList(requiresRoles.value());
                Logical logical = requiresRoles.logical();
                boolean[] booleans = subject.hasRoles(roles);
                List<Boolean> booleanList = Lists.newArrayListWithCapacity(booleans.length);
                for (boolean b : booleans) {
                    booleanList.add(b);
                }
                if (!checkLogical(logical, booleanList)) {
                    throw new UnauthorizedException(1, "");
                }
            }
            if (Objects2.nonNull(requiresPermissions)) {
                String[] permissions = requiresPermissions.value();
                boolean[] booleans = subject.isPermitted(permissions);
                Logical logical = requiresRoles.logical();
                List<Boolean> booleanList = Lists.newArrayListWithCapacity(booleans.length);
                for (boolean b : booleans) {
                    booleanList.add(b);
                }
                if (!checkLogical(logical, booleanList)) {
                    throw new UnauthorizedException(2, "");
                }
            }
        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            throw new UnauthorizedException(1, "");
        }
    }

    private boolean checkLogical(Logical logical, List<Boolean> booleanList) {
        if (logical.equals(Logical.AND)) {
            if (booleanList.contains(false)) {
                return false;
            }
        } else if (logical.equals(Logical.OR)) {
            if (booleanList.contains(true)) {
                return false;
            }
        }
        return true;
    }
}
