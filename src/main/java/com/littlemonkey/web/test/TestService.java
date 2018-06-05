package com.littlemonkey.web.test;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service("test")
public class TestService {

    public void get(@PathVariable @RequestBody  Integer name,  int id) {
        System.out.println("??????????????");
        System.out.println(id);
        System.out.println(name);
    }
}
