package com.webbrowser.bigwhite.utils;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.webbrowser.bigwhite.Model.SQLite.WebPageHelper;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OkHttpUtil {

    final static String TAG = "OkHttpUtils";

    public static String OkGetArt(String url) throws InterruptedException {
        final List<String> html = new ArrayList<>();
        Context context = WebPageHelper.webpagelist.get(0).getContext();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });
        String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1";
        String userAgent2 = getUserAgent(context);
        Request request = new Request.Builder()
                .url(url)
                .removeHeader("User-Agent")
                .addHeader("User-Agent", userAgent)
                .build();
//            Response response = client.newCall(request).execute();
        Call call = builder.build().newCall(request);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        call.enqueue(new Callback() {
                /**
                 * ?????????????????????
                 * @param call
                 * @param e
                 */
                @Override
                public void onFailure(Call call, IOException e) {
                    countDownLatch.countDown();
//                    Toast.makeText(,"??????get???????????????????????????",Toast.LENGTH_LONG).show();
                }

                /**
                 * ?????????????????????
                 * @param call
                 * @param response
                 * @throws IOException
                 */
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    html.add(res);
                    countDownLatch.countDown();
                }
            });

        //Log.i(TAG, "OkGetArt: html "+html);
        countDownLatch.await();
        return html.get(0);

    }
    public static String getUserAgent(@NonNull final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return WebSettings.getDefaultUserAgent(context);
        } else {
            try {
                final Class<?> webSettingsClassicClass = Class.forName("android.webkit.WebSettingsClassic");
                final Constructor<?> constructor = webSettingsClassicClass
                        .getDeclaredConstructor(Context.class, Class.forName("android.webkit.WebViewClassic"));
                constructor.setAccessible(true);
                final Method method = webSettingsClassicClass.getMethod("getUserAgentString");
                return (String) method.invoke(constructor.newInstance(context, null));
            } catch (final Exception e) {
                return new WebView(context).getSettings()
                        .getUserAgentString();
            }
        }
    }
    public static String getRealVideoUrl(String url){
        String pattern = ".*(https%253A.*)%2526innerIframe.*";

        // ?????? Pattern ??????
        Pattern r = Pattern.compile(pattern);

        // ???????????? matcher ??????
        Matcher m = r.matcher(url);
        if(m.find()){
            String s = m.group(1);
            return s.replace("%252F", "/").replace("%2526", "&")
                    .replace("%253D", "=").replace("%253F", "?")
                    .replace("%253A", ":");
        }
        return "https://activity.baidu.com/mbox/4a81af9f65/wiseFeed?appname=baiduboxvision&channel_id=1024359a&invoke_id=1024374a&reqid=23456&channel=wisefeed&vid=17662619466131416370&is_invoke=1";
    }
}