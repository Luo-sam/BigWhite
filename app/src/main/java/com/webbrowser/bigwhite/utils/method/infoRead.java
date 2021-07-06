package com.webbrowser.bigwhite.utils.method;

import android.webkit.WebView;

import com.webbrowser.bigwhite.Model.data.NewsData;
import com.webbrowser.bigwhite.utils.CrawlPageUtil;
import com.webbrowser.bigwhite.utils.OkHttpUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author luo
 * @Time 2021/7/5 15:12
 * @Description 获取data信息
 */
public class infoRead {
    public static void getData(WebView view) {
        if (view.getUrl().contains("https://www.baidu.com/#iact=wiseindex%")) {
            String viewUrl = view.getUrl();
            String pattern = "news_(\\d+)%";

            // 创建 Pattern 对象
            Pattern r = Pattern.compile(pattern);
            // 现在创建 matcher 对象
            Matcher m = r.matcher(viewUrl);
            if (m.find()) {
                String id = m.group(1);
                viewUrl = "https://mbd.baidu.com/newspage/data/landingpage?s_type=news&dsp=wise&context=%7B%22nid%22%3A%22news_" +
                        id +
                        "%22%7D&pageType=1&n_type=1&p_from=-1&quot";

                if (!CrawlPageUtil.newsMap.containsKey(viewUrl)) {
                    String html = null;
                    try {
                        html = OkHttpUtil.OkGetArt(viewUrl);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    NewsData newsData = CrawlPageUtil.spiderNewsData(html, viewUrl);
                    try {
                        CrawlPageUtil.newsMap.put(viewUrl, (NewsData) newsData.clone());
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    CrawlPageUtil.currentNews = newsData;
                } else
                    CrawlPageUtil.currentNews = CrawlPageUtil.newsMap.get(viewUrl);
            } else {
                // 默认是视频
                String html = null;
                try {
                    viewUrl = OkHttpUtil.getRealVideoUrl(viewUrl);
                    html = OkHttpUtil.OkGetArt(viewUrl);
                    CrawlPageUtil.videoData = CrawlPageUtil.spiderVideoUrl(html);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
