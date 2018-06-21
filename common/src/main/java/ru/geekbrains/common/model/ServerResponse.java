package ru.geekbrains.common.model;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {
    private T body;
    private RequestResult requestResult;
    private String message;

    public ServerResponse() {
    }

    public ServerResponse(T body, RequestResult requestResult, String message) {
        this.body = body;
        this.requestResult = requestResult;
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public RequestResult getRequestResult() {
        return requestResult;
    }

    public void setRequestResult(RequestResult requestResult) {
        this.requestResult = requestResult;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
