package com.bdd.api.builder;

import io.restassured.http.Headers;

import java.util.Map;

public class ApiConfig {
    private String apiType;
    private String apiURL;
    private String method;
    private Headers headers;
    private Map<String, Object> pathVariables;
    private Map<String, Object> params;
    private Map<String, Object> formUrlEncoded;
    private Map<String, Object> formData;
    private String body;

    public ApiConfig() {
    }

    public String getApiType() {
        return this.apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getApiURL() {
        return this.apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Headers getHeaders() {
        return this.headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public Map<String, Object> getPathVariables() {
        return this.pathVariables;
    }

    public void setPathVariables(Map<String, Object> pathVariables) {
        this.pathVariables = pathVariables;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getFormUrlEncoded() {
        return this.formUrlEncoded;
    }

    public void setFormUrlEncoded(Map<String, Object> formUrlEncoded) {
        this.formUrlEncoded = formUrlEncoded;
    }

    public Map<String, Object> getFormData() {
        return this.formData;
    }

    public void setFormData(Map<String, Object> formData) {
        this.formData = formData;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
