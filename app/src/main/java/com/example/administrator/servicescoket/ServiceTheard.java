package com.example.administrator.servicescoket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2017/4/27.
 */

public class ServiceTheard extends Thread {
    //socket对象
    Socket socket = null;
    //传递给客户端的数据
    String str = "1";
    //handler更新ui
    Handler handler;
    Context ctx;
    @Override
    public void run() {
        try {
            ServerSocket serivce = new ServerSocket(8888);
            while (true) {
                //等待客户端连接
                socket = serivce.accept();
                String line = null;
                InputStream input;
                OutputStream output;
                try {
                    //向客户端发送信息
                    output = socket.getOutputStream();
                    input = socket.getInputStream();
                    BufferedReader bff = new BufferedReader(
                            new InputStreamReader(input));
                    output.write(str.getBytes("utf-8"));
                    output.flush();
                    //半关闭socket
                    socket.shutdownOutput();
                    //获取客户端的信息
                    while ((line = bff.readLine()) != null) {
                        Message msg = new Message();
                        msg.obj = line;
                        handler.sendMessage(msg);
                    }
                    //关闭输入输出流
                    output.close();
                    bff.close();
                    input.close();
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {

        }
    }
}
