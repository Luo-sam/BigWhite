package com.webbrowser.bigwhite.Model.data;

import java.io.Serializable;

/**
 * 删除一个历史记录
 *
 * @author luo
 */
public class deleteThisHis implements Serializable {

    /**
     * state : {"code":1,"msg":"JWT String argument cannot be null or empty."}
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
         * code : 1
         * msg : JWT String argument cannot be null or empty.
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
