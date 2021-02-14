package com.example.libassistant;

public class BarCode<T> {
    public transient static final int TYPE_EXCNANGE=0;
    public transient static final int TYPE_WEBVIEW=1;
    private int type;
    private T data;

    public BarCode(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
