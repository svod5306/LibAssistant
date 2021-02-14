package com.example.libassistant;

public class ServiceReturnData<T> {
    public String status;
    public T data;

    public ServiceReturnData(String status,T data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
