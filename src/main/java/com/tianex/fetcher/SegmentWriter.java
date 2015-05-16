package com.tianex.fetcher;

import com.tianex.model.Content;
import com.tianex.model.CrawlDatum;
import com.tianex.parser.ParseResult;

/**
 * 爬取过程中，写入爬取历史、网页Content、解析信息的Writer
 * @author ex
 */
public interface SegmentWriter {

    /**
     * 写入一条爬去记录
     * @param fetch 爬取历史记录（爬取任务)
     * @throws Exception
     */
    public void wrtieFetch(CrawlDatum fetch) throws Exception;

    /**
     * 写入一条Content对象(存储网页/文件内容的对象)
     * @param content
     * @throws Exception
     */
    public void wrtieContent(Content content) throws Exception;

    /**
     * 写入一条网页解析结果
     * @param parseresult 网页解析结果
     * @throws Exception
     */
    public void wrtieParse(ParseResult parseresult) throws Exception;

    /**
     * 关闭Writer
     * @throws Exception
     */
    public void close() throws Exception;

}
