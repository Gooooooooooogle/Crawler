package com.tianex.net;

/**
 * 请求工厂接口类
 * @author ex
 */
public interface RequestFactory {
    public Request createRequest(String url) throws Exception;
}
