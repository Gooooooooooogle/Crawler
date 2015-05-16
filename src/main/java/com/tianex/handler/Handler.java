package com.tianex.handler;

/**
 * 用于传递和处理消息类
 * @author ex
 */
public class Handler {

    /**
     * 发送一条消息
     * @param msg 待发送消息
     */
    public void sendMessage(Message msg) {
        handleMessage(msg);
    }

    /**
     * 处理消息，用户可以通过Override这个方法，来自定义处理消息的方法
     * @param msg
     */
    public void handleMessage(Message msg) {

    }

}
