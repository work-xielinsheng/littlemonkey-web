package com.littlemonkey.web.request;

import java.io.Serializable;

/**
 * @Author: xls
 * @Description:
 * @Date: Created in 0:32 2018/3/28
 * @Version: 1.0
 */
public interface RequestBody extends Serializable {

    String getServiceName();

    String getMethodName();

    String getQueryString();

    String getContent();

}
