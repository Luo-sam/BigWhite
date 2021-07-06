package com.webbrowser.bigwhite.Model.data;


import java.io.Serializable;

public class simpleResponse implements Serializable {


    private StateDTO state;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public StateDTO getState() {
        return state;
    }

    public void setState(StateDTO state) {
        this.state = state;
    }

    private Object data;


    public static class StateDTO implements Serializable {
        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        private Integer code;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        private String msg;
    }
}
