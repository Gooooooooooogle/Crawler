package com.tianex.handler;

/**
 * 消息类
 * @author ex
 */
public class Message {

    /**
     * 消息附带的数据
     */
    public Object obj;

    /**
     * 消息的种类
     */
    public int what;

    public Message() {

    }

    /**
     * 构造一个消息
     * @param what 消息的种类
     * @param obj 消息附带的数据
     */
    public Message(int what, Object obj) {
        this.what = what;
        this.obj = obj;
    }
}
