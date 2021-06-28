package com.webbrowser.bigwhite.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.webbrowser.bigwhite.MainActivity;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
//        url = "https://mbd.baidu.com/newspage/data/landingpage?s_type=news&dsp=wise&context=%7B%22nid%22%3A%22news_9611519213472294428%22%7D&pageType=1&n_type=1&p_from=-1&rec_src=52&innerIframe=1";
        final List<String> html = new ArrayList<>();
//        OkHttpClient client = new OkHttpClient();
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
                 * 请求失败后执行
                 * @param call
                 * @param e
                 */
                @Override
                public void onFailure(Call call, IOException e) {
                    countDownLatch.countDown();
//                    Toast.makeText(,"异步get方式请求数据失败！",Toast.LENGTH_LONG).show();
                }

                /**
                 * 请求成功后执行
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
}