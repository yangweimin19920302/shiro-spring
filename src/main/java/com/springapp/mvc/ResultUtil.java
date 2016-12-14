package com.springapp.mvc;

/**
 * Created by Administrator on 2016/5/16.
 */
public class ResultUtil {
    private boolean status;
    private int code;
    private Object data;

    public ResultUtil(boolean status, int code, Object data) {
        this.status = status;
        this.code = code;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
