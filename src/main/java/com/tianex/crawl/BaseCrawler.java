package com.tianex.crawl;

import com.tianex.fetcher.Fetcher;
import com.tianex.generator.DbUpdater;
import com.tianex.generator.DbUpdaterFactory;
import com.tianex.generator.Generator;
import com.tianex.generator.Injector;
import com.tianex.handler.Handler;
import com.tianex.handler.Message;
import com.tianex.model.Page;
import com.tianex.net.RequestFactory;
import com.tianex.parser.ParserFactory;
import com.tianex.util.RegexRule;

import java.util.ArrayList;

/**
 * 广度遍历爬虫的基类
 * @author ex
 * @version 0.1
 */
public abstract class BaseCrawler implements RequestFactory, ParserFactory, DbUpdaterFactory {

    protected int status;
    public final static int RUNNING = 1;
    public final static int STOPED = 2;
    protected boolean resumable = false;
    protected int threads = 10;
    protected ArrayList<String> seeds = new ArrayList<String>();

    protected RegexRule regexRule = new RegexRule();
    protected Fetcher fetcher;

    public abstract Injector createInjector();
    public abstract Generator createGenerator();
    public abstract Fetcher createFetcher();

    /**
     * 开始深度为depth的爬取
     * @param depth 深度
     * @throws Exception
     */
    public void start(int depth) throws Exception {

        if (!resumable) {
            DbUpdater clearDbUpdater = createDbUpdater();
            if (clearDbUpdater != null) {
                clearDbUpdater.clearHistory();
            }
            if (seeds.isEmpty()) {
                return;
            }
        }

        if (regexRule.isEmpty()) {
            return;
        }

        inject();

        status = RUNNING;
        for (int i = 0; i < depth; i++) {
            if (status == STOPED) {
                break;
            }
            Generator generator = createGenerator();
            fetcher = createFetcher();
            fetcher = updateFetcher(fetcher);
            if (fetcher == null) {
                return;
            }
            fetcher.fetchAll(generator);
        }
    }

    protected Fetcher updateFetcher(Fetcher fetcher) {
        try {
            DbUpdater dbUpdater = createDbUpdater();
            if (dbUpdater != null) {
                fetcher.setDbUpdater(dbUpdater);
            }
            fetcher.setRequestFactory(this);
            fetcher.setParserFactory(this);
            fetcher.setHandler(createFetcherHandler());
            return fetcher;
        } catch (Exception ex) {
            return null;
        }
    }

    public void stop() throws Exception {
        fetcher.stop();
        status = STOPED;
    }

    public void inject() throws Exception {
        Injector injector = createInjector();
        injector.inject(seeds, true);
    }

    /**
     * 爬取成功时执行的方法
     * @param page 成功爬取的网页/文件
     */
    public void visit(Page page) {

    }

    /**
     * 爬取失败时执行的方法
     * @param page 爬取失败的网页/文件
     */
    public void failed(Page page) {

    }

    /**
     * 生成处理抓取消息的Handler，默认通过Crawler的visit方法来处理成功抓取的页面，
     * 通过failed方法来处理失败抓取的页面
     * @return 处理抓取消息的Handler
     */
    public Handler createFetcherHandler() {
        Handler fetchHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Page page = (Page) msg.obj;
                switch (msg.what) {
                    case Fetcher.FETCH_SUCCESS:
                        visit(page);
                        break;
                    case Fetcher.FETCH_FAILED:
                        failed(page);
                        break;
                    default:
                        break;
                }
            }
        };
        return fetchHandler;
    }

    public void addSeed(String seed) {
        seeds.add(seed);
    }

    public void addRegex(String regex) {
        regexRule.addRule(regex);
    }

    public boolean isResumable() {
        return resumable;
    }

    public void setResumable(boolean resumable) {
        this.resumable = resumable;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public RegexRule getRegexRule() {
        return regexRule;
    }

    public void setRegexRule(RegexRule regexRule) {
        this.regexRule = regexRule;
    }

    public ArrayList<String> getSeeds() {
        return seeds;
    }

    public void setSeeds(ArrayList<String> seeds) {
        this.seeds = seeds;
    }

}
