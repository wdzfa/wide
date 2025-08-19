package com.wdzfa.wide.dto;

import java.util.ArrayList;
import java.util.List;

public class ResponseData<T> {

    public boolean status;
    public List<String> message = new ArrayList<>();
    public T payload;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
