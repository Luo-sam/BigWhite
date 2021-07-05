package com.webbrowser.bigwhite.utils;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.webbrowser.bigwhite.activity.chooseLoginRegister;

public class MJavaScriptInterface {
    private Context context;

    public MJavaScriptInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void toActivity(){
        context.startActivity(new Intent(context, chooseLoginRegister.class));
    }

}
