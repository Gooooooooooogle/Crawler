package com.tianex.util;

import com.tianex.handler.Handler;
import com.tianex.handler.Message;

public class HandlerUtils {

    public static void sendMessage(Handler handler,Message msg){
        sendMessage(handler, msg,false);
    }

    public static void sendMessage(Handler handler,Message msg,boolean checknull){
        if(checknull){
            if(handler==null){
                return;
            }
        }
        handler.sendMessage(msg);
    }
}
