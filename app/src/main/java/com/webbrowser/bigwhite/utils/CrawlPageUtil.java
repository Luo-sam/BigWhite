package com.webbrowser.bigwhite.utils;

import android.util.Log;

import com.webbrowser.bigwhite.Model.data.NewsData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrawlPageUtil {
    private static final String TAG ="GetData" ;
    /*当前NewsData*/
    public static NewsData currentNews;
    /*<url, NewsData>键值对*/
    public static Map<String, NewsData> newsMap = new HashMap<>();
    /**
     * 抓取什么值得买首页的精选文章
     * @param html
     * @return  ArrayList<NewsData> NewsDatas
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
                    String s = element1.select("div").select("div[class~=^contentImg]").select("div")
                            .select("img").attr("src");
                    imageList.add(s);
                    textList.add(s);
                } else if(element1.attr("class").contains("contentText")){
                    if(element1.select("span") != null)
                        textList.add(element1.select("span").get(0).text());
                    else if(element1.select("p") != null)
                        textList.add(element1.select("p").get(0).text());
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

        return new NewsData(url, NewsTitle, author, textList, imageList);
    }
}