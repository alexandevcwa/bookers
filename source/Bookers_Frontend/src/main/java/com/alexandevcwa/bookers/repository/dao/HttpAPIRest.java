package com.alexandevcwa.bookers.repository.dao;

import com.alexandevcwa.bookers.model.enums.HttpMethod;

public class HttpAPIRest<T> {

    protected String doHttpRequest(String endpoint, HttpMethod httpMethod) {
        return null;
    }

    protected String doHttpRequest(String endpoint, HttpMethod httpMethod, String authorization) {
        return null;
    }

    protected String doHttpRequest(String endpoint, HttpMethod httpMethod, String authorization, T body) {
        return null;
    }

    protected String doHttpRequest(String endpoint, HttpMethod httpMethod, T body) {
        return null;
    }
}
