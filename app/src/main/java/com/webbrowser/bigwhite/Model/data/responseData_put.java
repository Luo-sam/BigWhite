package com.webbrowser.bigwhite.Model.data;


import java.io.Serializable;

public class responseData_put implements Serializable {

    /**
     * state : {"code":0,"msg":"ok"}
     * data : null
     */

    private StateBean state;
    private Object data;

    public StateBean getState() {
        return state;
    }

    public void setState(StateBean state) {
        this.state = state;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static class StateBean implements Serializable {
        /**
         * code : 0
         * msg : ok
         */

        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
