package com.webbrowser.bigwhite.Model.data;

import java.io.Serializable;
import java.util.List;

/**
 * 登录后responseData
 *
 * @author luo
 */
public class historyResponse implements Serializable {


    /**
     * state : {"code":0,"msg":"ok"}
     * data : [{"id":1,"title":"百度一下","url":"https://www.baidu.com/","date":"2021-06-16"},{"id":2,"title":"百度一下","url":"https://www.baidu.com/","date":"2021-06-16"},{"id":3,"title":"百度一下","url":"https://www.baidu.com/","date":"2021-06-16"},{"id":4,"title":"学习 - 百度","url":"https://www.baidu.com/s?wd=%E5%AD%A6%E4%B9%A0&ie=UTF-8","date":"2021-06-16"},{"id":5,"title":"百度一下","url":"https://www.baidu.com/","date":"2021-06-16"},{"id":6,"title":"百度一下","url":"https://www.baidu.com/","date":"2021-06-16"},{"id":7,"title":"百度一下","url":"https://www.baidu.com/","date":"2021-06-16"},{"id":8,"title":"https://www.baidu.com/","url":"https://www.baidu.com/","date":"2021-07-05"},{"id":9,"title":"\u201c我的奥运\u201d短视频征集活动等你参加","url":"https://baijiahao.baidu.com/s?id=1703326423376670373&wfr=spider&for=pc&searchword=%E2%80%9C%E6%88%91%E7%9A%84%E5%A5%A5%E8%BF%90%E2%80%9D%E7%9F%AD%E8%A7%86%E9%A2%91%E5%BE%81%E9%9B%86%E6%B4%BB%E5%8A%A8%E7%AD%89%E4%BD%A0%E5%8F%82%E5%8A%A0","date":"2021-07-05"},{"id":10,"title":"\u201c我的奥运\u201d短视频征集活动等你参加","url":"https://baijiahao.baidu.com/s?id=1703326423376670373&wfr=spider&for=pc&searchword=%E2%80%9C%E6%88%91%E7%9A%84%E5%A5%A5%E8%BF%90%E2%80%9D%E7%9F%AD%E8%A7%86%E9%A2%91%E5%BE%81%E9%9B%86%E6%B4%BB%E5%8A%A8%E7%AD%89%E4%BD%A0%E5%8F%82%E5%8A%A0","date":"2021-07-05"},{"id":11,"title":"\u201c我的奥运\u201d短视频征集活动等你参加","url":"https://baijiahao.baidu.com/s?id=1703326423376670373&wfr=spider&for=pc&searchword=%E2%80%9C%E6%88%91%E7%9A%84%E5%A5%A5%E8%BF%90%E2%80%9D%E7%9F%AD%E8%A7%86%E9%A2%91%E5%BE%81%E9%9B%86%E6%B4%BB%E5%8A%A8%E7%AD%89%E4%BD%A0%E5%8F%82%E5%8A%A0","date":"2021-07-05"},{"id":12,"title":"\u201c我的奥运\u201d短视频征集活动等你参加","url":"https://baijiahao.baidu.com/s?id=1703326423376670373&wfr=spider&for=pc&searchword=%E2%80%9C%E6%88%91%E7%9A%84%E5%A5%A5%E8%BF%90%E2%80%9D%E7%9F%AD%E8%A7%86%E9%A2%91%E5%BE%81%E9%9B%86%E6%B4%BB%E5%8A%A8%E7%AD%89%E4%BD%A0%E5%8F%82%E5%8A%A0","date":"2021-07-05"},{"id":13,"title":"bilibili_百度百科","url":"https://baike.baidu.com/item/bilibili/7056160?ivk_sa=1024630g","date":"2021-07-05"},{"id":14,"title":"bilibili_百度百科","url":"https://baike.baidu.com/item/bilibili/7056160?ivk_sa=1024630g","date":"2021-07-05"},{"id":15,"title":"加载中...","url":"https://www.baidu.com/s?wd=bibiilib&ie=UTF-8","date":"2021-07-05"},{"id":16,"title":"https://www.baidu.com/","url":"https://www.baidu.com/","date":"2021-07-05"},{"id":17,"title":"\u201c我的奥运\u201d短视频征集活动等你参加 - 百度","url":"https://www.baidu.com/s?wd=%E2%80%9C%E6%88%91%E7%9A%84%E5%A5%A5%E8%BF%90%E2%80%9D%E7%9F%AD%E8%A7%86%E9%A2%91%E5%BE%81%E9%9B%86%E6%B4%BB%E5%8A%A8%E7%AD%89%E4%BD%A0%E5%8F%82%E5%8A%A0&ie=UTF-8","date":"2021-07-05"},{"id":18,"title":"\u201c我的奥运\u201d短视频征集活动等你参加 - 百度","url":"https://www.baidu.com/s?wd=%E2%80%9C%E6%88%91%E7%9A%84%E5%A5%A5%E8%BF%90%E2%80%9D%E7%9F%AD%E8%A7%86%E9%A2%91%E5%BE%81%E9%9B%86%E6%B4%BB%E5%8A%A8%E7%AD%89%E4%BD%A0%E5%8F%82%E5%8A%A0&ie=UTF-8","date":"2021-07-05"},{"id":19,"title":"\u201c我的奥运\u201d短视频征集活动等你参加","url":"https://mbd.baidu.com/newspage/data/landingshare?pageType=1&nid=news_9536729835643983796&wfr=&_refluxos=","date":"2021-07-05"},{"id":20,"title":"bilibili - 百度","url":"https://www.baidu.com/s?wd=bilibili&ie=UTF-8","date":"2021-07-05"},{"id":21,"title":"bilibili - 百度","url":"https://www.baidu.com/s?wd=bilibili&ie=UTF-8","date":"2021-07-05"}]
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

    public static class DataBean implements Serializable {
        /**
         * id : 1
         * title : 百度一下
         * url : https://www.baidu.com/
         * date : 2021-06-16
         */

        private String url;
        private String date;
        private int id;
        private String title;

        public DataBean(String title, String url) {
            this.url = url;
            this.title = title;
        }

        public DataBean(String name, String address, int id){
            this.title = name;
            this.url = address;
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }
}
