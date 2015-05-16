package com.tianex.net;

import com.tianex.model.CrawlDatum;

import java.net.URL;

/**
 * Http请求的接口，如果用户需要自定义实现Http请求的类，需要实现这个接口
 * @author ex
 * @version 0.1
 */
public interface Request {
    public URL getURL();
    public void setURL(URL url);

    public Response getResponse(CrawlDatum datum) throws Exception;
}
