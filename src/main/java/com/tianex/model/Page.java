package com.tianex.model;

import com.tianex.net.Response;
import com.tianex.parser.ParseResult;
import org.jsoup.nodes.Document;

/**
 * Page是爬取过程中，内存中保存网页爬取信息的一个容器，与CrawlDatum不同，Page只在内存中存
 * 放，用于保存一些网页信息，方便用户进行自定义网页解析之类的操作。在广度遍历器中，用户覆盖
 * 的visit(Page page)方法，就是通过Page将网页爬取/解析信息传递给用户的。经过http请求、解
 * 析这些流程之后，page内保存的内容会越来越多
 *
 * @author ex
 */
public class Page {

    private long fetchTime;
    private String url = null;
    private String html = null;
    private Document doc = null;
    private Response response = null;
    private ParseResult parseResult = null;

    public byte[] getContent() {
        if(response == null) {
            return null;
        }
        return response.getContent();
    }

    public void setResponse(Response response){
        this.response=response;
    }

    public Response getResponse(){
        return response;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public long getFetchTime() {
        return fetchTime;
    }

    public void setFetchTime(long fetchTime) {
        this.fetchTime = fetchTime;
    }
    public ParseResult getParseResult() {
        return parseResult;
    }

    public void setParseResult(ParseResult parseResult) {
        this.parseResult = parseResult;
    }
}
