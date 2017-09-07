package com.example.administrator.servicescoket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.administrator.servicescoket.Textview.HTextView;
import com.example.administrator.servicescoket.Textview.HTextViewType;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Shake;

public class MainActivity extends Activity {
    //socket对象
    Socket socket = null;
    //传递给客户端的数据
    String str = "1";
    HTextView tv1;
    //handler更新ui
    Handler handler;
    //webview加载连接
    WebView webView;
    //弹出对话框
    NiftyDialogBuilder dialogBuilder;
    //输入输出流
    InputStream input;
    OutputStream output;
    BufferedReader bff;
    String strLine = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (HTextView) findViewById(R.id.tvOne);
        webView = (WebView) findViewById(R.id.webview);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
        ServiceScoket();
        webview("http://a3.rabbitpre.com/m/fEnFBeMQy");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //@0@标识更新下方广告
                if (msg.obj.toString().indexOf("@0@") != -1) {
                    String str = msg.obj.toString().substring(3);
                    webview(str);
                    dialogBuilder.dismiss();
                }
                //@1@表示数据
                if (msg.obj.toString().indexOf("@1@") != -1) {
                    String str = msg.obj.toString().substring(3);
                    tv1.setAnimateType(HTextViewType.ANVIL);
                    tv1.animateText(str); // animate
                    dialogBuilder.dismiss();
                }
            }
        };
    }

    //webView加载网页
    public void webview(String str) {
        WebSettings setting = webView.getSettings();
        setSettings(setting);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.clearCache(true);
        webView.clearHistory();
        //不可点击
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        webView.loadUrl(str);
    }

    //服务端
    public void ServiceScoket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serivce = new ServerSocket(8888);
                    while (true) {
                        //等待客户端连接
                        socket = serivce.accept();
                        String line = null;
                        try {
                            //向客户端发送信息
                            output = socket.getOutputStream();
                            input = socket.getInputStream();
                            bff = new BufferedReader(
                                    new InputStreamReader(input));
                            output.write(str.getBytes("utf-8"));
                            output.flush();
                            //半关闭socket
                            socket.shutdownOutput();
                            //获取客户端的信息
                            while ((line = bff.readLine()) != null) {
                                //如果跟发来的字符串相当那么不需要更新ui，反之更新ui
                                if (strLine.equals(line)) {

                                } else {
                                    strLine = line;
                                    Message msg = new Message();
                                    msg.obj = line;
                                    handler.sendMessage(msg);
                                    Log.e("str", line);
                                    Log.e("strLine", strLine);
                                }
                            }
                            close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {

                }
            }
        }).start();
    }

    //webview相关设置
    @SuppressLint("NewApi")
    private void setSettings(WebSettings setting) {
        try {
            setting.setJavaScriptEnabled(true);
            setting.setBuiltInZoomControls(true);
            setting.setDisplayZoomControls(false);
            setting.setSupportZoom(true);
            setting.setDomStorageEnabled(true);
            setting.setDatabaseEnabled(true);
            // 全屏显示
            setting.setLoadWithOverviewMode(true);
            setting.setUseWideViewPort(true);

        } catch (SecurityException s) {
            Log.e("webview", 1 + "");
        } finally {

        }
    }

    //是否退出对话框弹出方法
    public void showDialog() {
        dialogBuilder
                .withTitle("提示")
                .withTitleColor("#FFFFFF")
                .withDividerColor("#11000000")
                .withMessage("\n" + "确认退出吗？\n")
                //设置文字颜色
                .withMessageColor("#FFFFFF")
                .withButton1Text("确认")
                .withButton2Text("取消")
                //设置对话框整体颜色
                .withDialogColor("#d4cece")
                //设置弹出对话框的类型
                .withEffect(Shake)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }

                }).setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        }).show();
    }

    //返回按钮按下事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            showDialog();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (socket != null) {
                //关闭输入输出流
                output.close();
                bff.close();
                input.close();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        close();
    }

}


