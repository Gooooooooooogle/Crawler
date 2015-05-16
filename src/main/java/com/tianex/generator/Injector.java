package com.tianex.generator;

import java.util.List;

/**
 * 广度遍历的种子注入器
 * @author ex
 */
public interface Injector {

    /**
     * 以新建的方式，注入一个种子url
     * @param url 种子url
     * @throws Exception
     */
    public void inject(String url) throws Exception;

    /**
     * 以新建的方式，注入种子url列表
     * @param urls 种子url列表
     * @throws Exception
     */
    public void inject(List<String> urls) throws Exception;

    /**
     * 以新建/追加的方式，注入一个种子url
     * @param url 种子url
     * @param append 是否追加
     * @throws Exception
     */
    public void inject(String url, boolean append) throws Exception;


    /**
     * 以新建/追加方式注入种子url列表
     * @param urls 种子url列表
     * @param append 是否追加
     * @throws Exception
     */
    public void inject(List<String> urls, boolean append) throws Exception;
}
