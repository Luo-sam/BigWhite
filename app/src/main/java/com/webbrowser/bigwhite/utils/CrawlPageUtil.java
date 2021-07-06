package com.webbrowser.bigwhite.utils;

import android.util.Log;

import com.webbrowser.bigwhite.Model.data.NewsData;
import com.webbrowser.bigwhite.Model.data.VideoData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlPageUtil {
    private static final String TAG ="GetData" ;
    /*当前NewsData*/
    public static NewsData currentNews;
    /*<url, NewsData>键值对*/
    public static Map<String, NewsData> newsMap = new HashMap<>();
    /*当前视频*/
    public static VideoData videoData;
    /**
     * 抓取图文
     * @param html，url
     * @return  NewsDatas
     */
    public static NewsData spiderNewsData(String html, String url){

        Document document = Jsoup.parse(html);
        String NewsTitle = document.select("h1[class=titleSize]").text();
        String author = document.select("span[class=afrSplit]").text();

        Element element = document
                .select("div[class=mainContent]").get(0);
        Elements div = element.select("div[class~=^content]");

        List<String> textList = new ArrayList<>();
        List<String> imageList = new ArrayList<>();

        for (Element element1 : div) {
            try {
                if(element1.attr("class").contains("contentMedia")){
                    String s = null;
                    if(element1.select("div").select("div[class~=^contentImg]").size()!=0) {
                        s = element1.select("div").select("div[class~=^contentImg]").select("div")
                                .select("img").attr("src");
                    }
                    else if(element1.select("div").select("div[class~=^shareContentImg]").size()!=0){
                        s = element1.select("div").select("div[class~=^shareContentImg]").select("div")
                                .select("img").attr("src");
                    }
                    if(s!=null) {
                        imageList.add(s);
                        textList.add(s);
                    }
                } else if(element1.attr("class").contains("contentText")){
                    if(element1.select("span").size() != 0)
                        textList.add(element1.select("span").get(0).text());
                    else if(element1.select("p").size() != 0)
                        textList.add(element1.select("p").get(0).text());
                    else if(element1.select("article").size() != 0){
                        Element article = element1.select("article").get(0);
                        Elements elements = article.getAllElements();
                        for (Element element2 : elements) {
                            if(element2.select("p").size() != 0){
                                textList.add(element2.select("p").get(0).text());
                            }
                            else if(element2.select("div").size()!=0){
                                if(element2.select("div").select("span").size()!=0&&
                                        element2.select("div").select("span").select("img").size()!=0)
                                    textList.add(element2.select("div").select("span").select("img").attr("src"));
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
            }

        }

//        Log.i(TAG, "spiderNewsData: elements " +element.html());
//        Elements texts = element.select("div[class~=^contentText contentSize]");
//        Elements medias = element.select("div[class~=^contentMedia]");
//
//        for(Element element1: texts){
//            textList.add(element1.select("span").get(0).text());
//        }
//        for(Element element1: medias){
//            imageList.add(element1.select("div").select("div[class~=^contentImg]").select("div")
//            .select("img").attr("src"));
//        }

        return new NewsData(url, NewsTitle, author, textList, imageList, null);
    }

    /**
     * 抓取视频
     * @param html
     * @return  String
     */
    public static VideoData spiderVideoUrl(String html){
        String url_pattern = "mainVideoList.*\"videoUrl\":\"(https:.*mp4)\"";
        String title_pattern = "mainVideoList.*\"title\":\"(.*?)\"";
        String author_pattern = "mainVideoList.*\"authorName\":\"(.*?)\"";
        VideoData videoData = new VideoData("https://vd2.bdstatic.com/mda-mfdi9f9t3hjq1rx3/cae_h264/1623675468157378528/mda-mfdi9f9t3hjq1rx3.mp4",
                "大白", "大白");
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(url_pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(html);
        if(m.find()) {
            String s = m.group(1);
            s = s.replace("\\", "");
            videoData.setVideoUrl(s);
        }

        r = Pattern.compile(title_pattern);
        m = r.matcher(html);
        if(m.find()) {
            String s = m.group(1);
            videoData.setTitle(s);
        }

        r = Pattern.compile(author_pattern);
        m = r.matcher(html);
        if(m.find()) {
            String s = m.group(1);
            videoData.setAuthor(s);
        }

        return videoData;
    }
}