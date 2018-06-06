package com.littlemonkey.web.controller;

import com.littlemonkey.web.context.CurrentHttpServletHolder;
import com.littlemonkey.web.method.Method;
import com.littlemonkey.web.request.DefaultRequestBody;
import com.littlemonkey.web.request.RestfulRequestBody;
import com.littlemonkey.web.utils.WebUtils2;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ApiController extends BaseController {

    /**
     * <p>get list</p>
     *
     * @param serviceName
     */
    @GetMapping(value = "/{serviceName}")
    public void doGetList(@PathVariable(name = "serviceName") String serviceName) throws Exception {
        RestfulRequestBody restfulRequestBody = new RestfulRequestBody(serviceName,
                Method.list.name(), null, CurrentHttpServletHolder.getCurrentRequest().getQueryString(), null);
        this.processRequest(RequestMethod.GET, restfulRequestBody);
    }

    /**
     * <p>get item</p>
     *
     * @param serviceName
     * @param id
     */
    @GetMapping(value = "/{serviceName}/{id}")
    public void doGetItem(@PathVariable(name = "serviceName") String serviceName, @PathVariable(name = "id") Long id) throws Exception {
        RestfulRequestBody restfulRequestBody = new RestfulRequestBody(serviceName, Method.get.name(), null,
                WebUtils2.getQueryString(CurrentHttpServletHolder.getCurrentRequest()), id);
        restfulRequestBody.setId(id);
        this.processRequest(RequestMethod.GET, restfulRequestBody);
    }

    /**
     * <p>add item</p>
     *
     * @param serviceName
     */
    @PostMapping(value = "/{serviceName}", consumes = "application/json")
    public void doPost(@PathVariable(name = "serviceName") String serviceName) throws Exception {
        RestfulRequestBody restfulRequestBody = new RestfulRequestBody(serviceName, Method.save.name(),
                WebUtils2.getRequestBodyJson(CurrentHttpServletHolder.getCurrentRequest()), null, null);
        this.processRequest(RequestMethod.POST, restfulRequestBody);
    }

    /**
     * <p>update item</p>
     *
     * @param serviceName
     */
    @PutMapping(value = "/{serviceName}/{id}", consumes = "application/json")
    public void doPut(@PathVariable(name = "serviceName") String serviceName, @PathVariable(name = "id") Long id) throws Exception {
        RestfulRequestBody restfulRequestBody = new RestfulRequestBody(serviceName, Method.update.name(),
                WebUtils2.getRequestBodyJson(CurrentHttpServletHolder.getCurrentRequest()), null, id);
        restfulRequestBody.setId(id);
        this.processRequest(RequestMethod.PUT, restfulRequestBody);
    }

    /**
     * <p>delete item</p>
     *
     * @param serviceName
     * @param id
     */
    @DeleteMapping(value = "/{serviceName}/{id}")
    public void doDelete(@PathVariable(name = "serviceName") String serviceName, @PathVariable(name = "id") Long id) throws Exception {
        RestfulRequestBody restfulRequestBody = new RestfulRequestBody(serviceName, Method.remove.name(), null,
                WebUtils2.getQueryString(CurrentHttpServletHolder.getCurrentRequest()), id);
        restfulRequestBody.setId(id);
        this.processRequest(RequestMethod.DELETE, restfulRequestBody);
    }

    /**
     * <p>other get request</p>
     *
     * @param serviceName
     * @param methodName
     */
    @GetMapping(value = "/{serviceName}/{methodName}.other")
    public void otherGet(@PathVariable(name = "serviceName") String serviceName, @PathVariable(name = "methodName") String methodName) throws Exception {
        DefaultRequestBody defaultRequestBody = new DefaultRequestBody(serviceName, methodName, null,
                WebUtils2.getQueryString(CurrentHttpServletHolder.getCurrentRequest()));
        this.processRequest(RequestMethod.GET, defaultRequestBody);
    }

    /**
     * <p>other post request</p>
     *
     * @param serviceName
     * @param methodName
     */
    @PostMapping(value = "/{serviceName}/{methodName}.other", consumes = "application/json")
    public void otherPost(@PathVariable(name = "serviceName") String serviceName, @PathVariable(name = "methodName") String methodName) throws Exception {
        DefaultRequestBody defaultRequestBody = new DefaultRequestBody(serviceName, methodName,
                WebUtils2.getRequestBodyJson(CurrentHttpServletHolder.getCurrentRequest()), null);
        this.processRequest(RequestMethod.POST, defaultRequestBody);
    }
}
