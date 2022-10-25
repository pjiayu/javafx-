package com.xwintop.xJavaFxTool.model.vo;

public class MsgEntity<T> {
    private String state;
    private String code;
    private T msg;
    private String transmsg;

    public String getTransmsg() {
        return transmsg;
    }

    public void setTransmsg(String transmsg) {
        this.transmsg = transmsg;
    }

    public MsgEntity(String state, String code, T msg, String transmsg) {
        this.state = state;
        this.code = code;
        this.msg = msg;
        this.transmsg=transmsg;
    }
    public MsgEntity(String state, String code,String transmsg) {
        this.state = state;
        this.code = code;
        this.msg = null;
        this.transmsg=transmsg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }
}
