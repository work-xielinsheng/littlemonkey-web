package com.littlemonkey.web.annotation;

import org.springframework.stereotype.Component;

@Component
public @interface Resource {
    String name();
}
