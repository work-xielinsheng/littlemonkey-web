package com.littlemonkey.web.controller;

import com.littlemonkey.web.context.CurrentHttpServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class WebControllerAdvice {

    private final static Logger logger = LoggerFactory.getLogger(WebControllerAdvice.class);

    @ModelAttribute
    public void setCurrentRequestAndCurrentResponse(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Content-Type :" + request.getContentType() + " =========================");
        CurrentHttpServletHolder.setCurrentRequestAndCurrentResponse(request, response);
    }

}
