package com.littlemonkey.web.method.build.impl;

import com.littlemonkey.web.method.build.MethodBuildProvider;
import com.littlemonkey.web.param.RequestDetail;
import org.springframework.stereotype.Component;

@Component("defaultMethodBuildProviderImpl")
public class DefaultMethodBuildProviderImpl implements MethodBuildProvider {
    @Override
    public Object[] buildParams(RequestDetail requestDetail) {
        return this.resolve(requestDetail);
    }
}
