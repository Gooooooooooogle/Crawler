package com.tianex.parser;

/**
 * 解析器工厂接口类
 * @author ex
 */
public interface ParserFactory {
    public Parser createParser(String url, String contentType) throws Exception;
}
