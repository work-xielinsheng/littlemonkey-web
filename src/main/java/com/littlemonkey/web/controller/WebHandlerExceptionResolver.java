package com.littlemonkey.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.littlemonkey.web.context.CurrentHttpServletHolder;
import com.littlemonkey.web.exception.ApplicationException;
import com.littlemonkey.web.response.Answer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class WebHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        HttpServletResponse response = CurrentHttpServletHolder.getCurrentResponse();
        Answer answer = new Answer();
        answer.setServiceName(CurrentHttpServletHolder.getCurrentServerName());
        answer.setMethodName(CurrentHttpServletHolder.getCurrentMethodName());
        if (e instanceof ApplicationException) {
            ApplicationException applicationException = (ApplicationException) e;
            answer.setCode(applicationException.getErrorCode());
            answer.setMessage(applicationException.getMessage());
            response.setStatus(applicationException.getErrorCode());
        }
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(answer);
        modelAndView.addAllObjects(jsonObject);
        return modelAndView;
    }
}
