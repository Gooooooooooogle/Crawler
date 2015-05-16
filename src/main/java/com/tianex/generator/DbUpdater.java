package com.tianex.generator;

import com.tianex.fetcher.SegmentWriter;

/**
 * 用于更新爬取任务列表的接口类
 * @author ex
 */
public interface DbUpdater {

    public void lock() throws Exception;
    public void unlock() throws Exception;
    public boolean isLocked() throws Exception;

    public void initSegmentWriter() throws Exception;
    public void close() throws Exception;
    public void merge() throws Exception;

    public SegmentWriter getSegmentWriter();
    public void clearHistory();

}
