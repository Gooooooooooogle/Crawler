package com.tianex.parser;

/**
 * 网页解析后，存储网页解析文本的类
 * @author ex
 */
public class ParseText {

    private String url = null;
    private String text = null;

    public ParseText() {
    }

    public ParseText(String url,String text) {
        this.url=url;
        this.text=text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
