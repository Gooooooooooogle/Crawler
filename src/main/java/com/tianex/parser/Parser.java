package com.tianex.parser;

import com.tianex.model.Page;

/**
 * 网页解析器接口，用户如果需要自定义网页解析器，必须实现这个接口
 * @author ex
 */
public interface Parser {

    /**
     * 对指定页面进行解析，返回解析结果
     * @param page 待解析页面
     * @return 解析结果
     * @throws Exception
     */
    public ParseResult getParse(Page page) throws Exception;

}
