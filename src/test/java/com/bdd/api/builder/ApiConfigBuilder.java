package com.bdd.api.builder;

import io.restassured.http.Headers;

import java.util.Map;

public class ApiConfigBuilder implements IApiConfig{
    private String apiType;
    private String apiURL;
    private String method;
    private Headers headers;
    private Map<String, Object> pathVariables;
    private Map<String, Object> params;
    private Map<String, Object> formUrlEncoded;
    private Map<String, Object> formData;
    private String body;

    public ApiConfigBuilder() {
    }

    public ApiConfigBuilder withApiType(String apiType) {
        this.apiType = apiType;
        return this;
    }

    public ApiConfigBuilder withApiURL(String apiURL) {
        this.apiURL = apiURL;
        return this;
    }

    public ApiConfigBuilder withMethod(String method) {
        this.method = method;
        return this;
    }

    public ApiConfigBuilder withHeaders(Headers headers) {
        this.headers = headers;
        return this;
    }

    public ApiConfigBuilder withPathVariables(Map<String, Object> pathVariables) {
        this.pathVariables = pathVariables;
        return this;
    }

    public ApiConfigBuilder withParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public ApiConfigBuilder withFormUrlEncoded(Map<String, Object> formUrlEncoded) {
        this.formUrlEncoded = formUrlEncoded;
        return this;
    }

    public ApiConfigBuilder withFormData(Map<String, Object> formData) {
        this.formData = formData;
        return this;
    }

    public ApiConfigBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public ApiConfig build() {
        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setApiType(this.apiType);
        apiConfig.setApiURL(this.apiURL);
        apiConfig.setMethod(this.method);
        apiConfig.setHeaders(this.headers);
        apiConfig.setPathVariables(this.pathVariables);
        apiConfig.setParams(this.params);
        apiConfig.setFormUrlEncoded(this.formUrlEncoded);
        apiConfig.setFormData(this.formData);
        apiConfig.setBody(this.body);
        return apiConfig;
    }

}
