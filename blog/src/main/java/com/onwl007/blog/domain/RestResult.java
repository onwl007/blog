package com.onwl007.blog.domain;

import com.alibaba.fastjson.JSON;

/**
 * 统一封装API返回信息
 * 千万别加@Entity 否则Hibernate会给你创建表
 */
public class RestResult {
    private int code;
    private String message;
    private Object data;

    public RestResult setCode(RestResult restResult){
        this.code=restResult.getCode();
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public RestResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RestResult setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
