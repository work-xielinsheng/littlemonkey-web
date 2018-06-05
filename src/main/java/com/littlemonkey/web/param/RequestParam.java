package com.littlemonkey.web.param;

public class RequestParam<T> {

    private int start;
    private int limit;
    private T body;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "RequestParam{" +
                "start=" + start +
                ", limit=" + limit +
                ", body=" + body +
                '}';
    }
}
