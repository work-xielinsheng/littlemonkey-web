package com.littlemonkey.web.method.build.impl;

import com.google.common.collect.Lists;
import com.littlemonkey.utils.base.Constants;
import com.littlemonkey.utils.collect.Collections3;
import com.littlemonkey.utils.lang.StringUtils;
import com.littlemonkey.web.common.ValueConstants;
import com.littlemonkey.web.method.MethodDetail;
import com.littlemonkey.web.method.build.MethodBuildProvider;
import com.littlemonkey.web.param.RequestDetail;
import com.littlemonkey.web.request.RestfulRequestBody;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("restfulMethodBuildProviderImpl")
public class RestfulMethodBuildProviderImpl implements MethodBuildProvider {
    @Override
    public Object[] buildParams(RequestDetail requestDetail) {
        RestfulRequestBody restfulRequestBody = (RestfulRequestBody) requestDetail.getRequestBody();
        if (requestDetail.getRequestMethodType().equals(RequestMethod.GET)
                || requestDetail.getRequestMethodType().equals(RequestMethod.PUT)
                || requestDetail.getRequestMethodType().equals(RequestMethod.DELETE)) {
            List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(restfulRequestBody.getQueryString(), StandardCharsets.UTF_8);
            if (Collections3.isEmpty(nameValuePairs)) {
                nameValuePairs = Lists.newArrayListWithCapacity(1);
            }
            nameValuePairs.add(new BasicNameValuePair(ValueConstants.ID, String.valueOf(restfulRequestBody.getId())));
            final Map<String, String> queryMap = nameValuePairs.stream().collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
            restfulRequestBody.setQueryString(StringUtils.join(queryMap, Constants.AD, Constants.EQUAL));
        }
        return this.resolve(requestDetail);
    }
}
