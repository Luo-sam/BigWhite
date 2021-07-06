package com.webbrowser.bigwhite.Model.data;

import java.io.Serializable;
import java.util.List;

/**
 * bookmark的结果
 *
 * @author luo
 */
public class bookmarkResponse implements Serializable {


    /**
     * state : {"code":0,"msg":"查询书签成功"}
     * data : [{"id":1,"user":{"id":6,"username":"luo-sam","password":"31db519e5dafa1ba4c782a8d1b4370ef0c826290b13f85e7541afd625007340c","email":"2532467954@qq.com","salt":"6Tuw91DMJg6XCGednErpRA==","avatar":"/usr/local/bigwhite\\main\\resources\\user_images\\默认头像xxx.png"},"tag":"learn","title":"\u201c我的奥运\u201d短视频征集活动等你参加","url":"https://mbd.baidu.com/newspage/data/landingshare?pageType=1&nid=news_9536729835643983796&wfr=&_refluxos="},{"id":2,"user":{"id":6,"username":"luo-sam","password":"31db519e5dafa1ba4c782a8d1b4370ef0c826290b13f85e7541afd625007340c","email":"2532467954@qq.com","salt":"6Tuw91DMJg6XCGednErpRA==","avatar":"/usr/local/bigwhite\\main\\resources\\user_images\\默认头像xxx.png"},"tag":"leran","title":"llearn - 百度","url":"https://www.baidu.com/s?wd=llearn&ie=UTF-8"},{"id":3,"user":{"id":6,"username":"luo-sam","password":"31db519e5dafa1ba4c782a8d1b4370ef0c826290b13f85e7541afd625007340c","email":"2532467954@qq.com","salt":"6Tuw91DMJg6XCGednErpRA==","avatar":"/usr/local/bigwhite\\main\\resources\\user_images\\默认头像xxx.png"},"tag":"leran","title":"llearn - 百度","url":"https://www.baidu.com/s?wd=llearn&ie=UTF-8"},{"id":4,"user":{"id":6,"username":"luo-sam","password":"31db519e5dafa1ba4c782a8d1b4370ef0c826290b13f85e7541afd625007340c","email":"2532467954@qq.com","salt":"6Tuw91DMJg6XCGednErpRA==","avatar":"/usr/local/bigwhite\\main\\resources\\user_images\\默认头像xxx.png"},"tag":"leran","title":"llearn - 百度","url":"https://www.baidu.com/s?wd=llearn&ie=UTF-8"},{"id":5,"user":{"id":6,"username":"luo-sam","password":"31db519e5dafa1ba4c782a8d1b4370ef0c826290b13f85e7541afd625007340c","email":"2532467954@qq.com","salt":"6Tuw91DMJg6XCGednErpRA==","avatar":"/usr/local/bigwhite\\main\\resources\\user_images\\默认头像xxx.png"},"tag":"leran","title":"llearn - 百度","url":"https://www.baidu.com/s?wd=llearn&ie=UTF-8"},{"id":6,"user":{"id":6,"username":"luo-sam","password":"31db519e5dafa1ba4c782a8d1b4370ef0c826290b13f85e7541afd625007340c","email":"2532467954@qq.com","salt":"6Tuw91DMJg6XCGednErpRA==","avatar":"/usr/local/bigwhite\\main\\resources\\user_images\\默认头像xxx.png"},"tag":"leran","title":"知了好学-专业教育培训服务平台","url":"https://xue.baidu.com/okam/pages/online-course/index?source=39259&sa=39259&courseId=124832798827735&appKey=Tv397hxPNa5PYBBAHds139ymOm3v2nsQ_ext20210623160158"},{"id":7,"user":{"id":6,"username":"luo-sam","password":"31db519e5dafa1ba4c782a8d1b4370ef0c826290b13f85e7541afd625007340c","email":"2532467954@qq.com","salt":"6Tuw91DMJg6XCGednErpRA==","avatar":"/usr/local/bigwhite\\main\\resources\\user_images\\默认头像xxx.png"},"tag":"leran","title":"llearn - 百度","url":"https://www.baidu.com/s?wd=llearn&ie=UTF-8"},{"id":8,"user":{"id":6,"username":"luo-sam","password":"31db519e5dafa1ba4c782a8d1b4370ef0c826290b13f85e7541afd625007340c","email":"2532467954@qq.com","salt":"6Tuw91DMJg6XCGednErpRA==","avatar":"/usr/local/bigwhite\\main\\resources\\user_images\\默认头像xxx.png"},"tag":"leran","title":"llearn - 百度","url":"https://www.baidu.com/s?wd=llearn&ie=UTF-8"},{"id":9,"user":{"id":6,"username":"luo-sam","password":"31db519e5dafa1ba4c782a8d1b4370ef0c826290b13f85e7541afd625007340c","email":"2532467954@qq.com","salt":"6Tuw91DMJg6XCGednErpRA==","avatar":"/usr/local/bigwhite\\main\\resources\\user_images\\默认头像xxx.png"},"tag":"leran","title":"learn的用法","url":"https://zhidao.baidu.com/question/2016166362399531588.html"}]
     */

    private StateBean state;
    private List<DataBean> data;

    public StateBean getState() {
        return state;
    }

    public void setState(StateBean state) {
        this.state = state;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class StateBean implements Serializable {
        /**
         * code : 0
         * msg : 查询书签成功
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


    public static class DataBean implements Serializable {
        /**
         * id : 1
         * user : {"id":6,"username":"luo-sam","password":"31db519e5dafa1ba4c782a8d1b4370ef0c826290b13f85e7541afd625007340c","email":"2532467954@qq.com","salt":"6Tuw91DMJg6XCGednErpRA==","avatar":"/usr/local/bigwhite\\main\\resources\\user_images\\默认头像xxx.png"}
         * tag : learn
         * title : “我的奥运”短视频征集活动等你参加
         * url : https://mbd.baidu.com/newspage/data/landingshare?pageType=1&nid=news_9536729835643983796&wfr=&_refluxos=
         */

        private int id;
        private UserBean user;
        private String tag;
        private String title;
        private String url;


        public DataBean(int id, String title, String url) {
            this.id = id;
            this.title = title;
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public static class UserBean implements Serializable {
            /**
             * id : 6
             * username : luo-sam
             * password : 31db519e5dafa1ba4c782a8d1b4370ef0c826290b13f85e7541afd625007340c
             * email : 2532467954@qq.com
             * salt : 6Tuw91DMJg6XCGednErpRA==
             */

            private int id;
            private String username;
            private String password;
            private String email;
            private String salt;
            private String avatar;
        }
    }
}
