package com.tianex.generator;

import com.tianex.model.CrawlDatum;

/**
 * 抓取任务生成器
 * @author ex
 */
public interface Generator {

    /**
     * 获取下一个抓取任务
     * @return 下一个抓取任务， 若无则返回null
     */
    CrawlDatum next();
}
