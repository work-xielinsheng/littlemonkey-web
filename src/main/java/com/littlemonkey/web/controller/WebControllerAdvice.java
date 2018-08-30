package com.littlemonkey.web.controller;

import com.littlemonkey.web.context.CurrentHttpServletHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Slf4j
public class WebControllerAdvice {

    @ModelAttribute
    public void setCurrentRequestAndCurrentResponse(HttpServletRequest request, HttpServletResponse response) {
        log.info("Content-Type :" + request.getContentType() + " =========================");
        CurrentHttpServletHolder.setCurrentRequestAndCurrentResponse(request, response);
    }

}
