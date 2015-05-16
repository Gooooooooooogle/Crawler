package com.tianex.model;

/**
 * 保存网页链接的类
 * @author ex
 */
public class Link {

    /**
     * 链接的锚文本
     */
    private String anchor;

    /**
     * 链接的url
     */
    private String url;

    public Link() {
    }

    public Link(String anchor, String url) {
        this.anchor = anchor;
        this.url = url;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
