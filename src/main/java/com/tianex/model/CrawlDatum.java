package com.tianex.model;

/**
 * 存储爬取任务的类，是WebCollector的核心类
 * 其记录了一个url的爬取信息，同样也可以作为一个爬取任务
 * @author ex
 * @version 0.1
 */
public class CrawlDatum {
    public static final int STATUS_DB_UNDEFINED = -1;  //爬取状态常量-未定义
    public static final int STATUS_DB_UNFETCHED = 1;  //爬取状态常量-未爬取
    public static final int STATUS_DB_FETCHED = 2;  //爬取状态常量-已爬取
    public static final long FETCHTIME_UNDEFINED = 1;  //爬取时间常量-未定义

    private String url;
    private int status = CrawlDatum.STATUS_DB_UNDEFINED;
    private long fetchTime = CrawlDatum.FETCHTIME_UNDEFINED;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public long getFetchTime() {
        return fetchTime;
    }
    public void setFetchTime(long fetchTime) {
        this.fetchTime = fetchTime;
    }
}
