package com.example.lib.tasks;

public class UploadResult {
    private int code;
    private String msg;

    public UploadResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
