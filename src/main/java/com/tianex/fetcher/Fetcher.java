package com.tianex.fetcher;

import com.tianex.generator.DbUpdater;
import com.tianex.generator.Generator;
import com.tianex.handler.Handler;
import com.tianex.handler.Message;
import com.tianex.model.Content;
import com.tianex.model.CrawlDatum;
import com.tianex.model.Page;
import com.tianex.net.Request;
import com.tianex.net.RequestFactory;
import com.tianex.net.Response;
import com.tianex.parser.ParseResult;
import com.tianex.parser.Parser;
import com.tianex.parser.ParserFactory;
import com.tianex.util.Config;
import com.tianex.util.HandlerUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 抓取器
 * @author ex
 */
public class Fetcher {
    public DbUpdater dbUpdater = null;
    public Handler handler = null;
    public RequestFactory requestFactory = null;
    public ParserFactory parserFactory = null;
    private int retry = 3;
    private AtomicInteger activeThreads;
    private AtomicInteger spinWaiting;
    private AtomicLong lastRequestStart;
    private QueueFeeder feeder;
    private FetchQueue fetchQueue;
    private boolean needUpdateDb = true;
    public static final int FETCH_SUCCESS = 1;
    public static final int FETCH_FAILED = 2;
    private int threads = 10;
    private boolean isContentStored = true;
    private boolean parsing = true;

    public static class FetchItem {

        public CrawlDatum datum;

        public FetchItem(CrawlDatum datum) {
            this.datum = datum;
        }

    }

    public static class FetchQueue {

        public AtomicInteger totalSize = new AtomicInteger();
        public List<FetchItem> queue = Collections.synchronizedList(new LinkedList<FetchItem>());

        public synchronized void clear() {
            queue.clear();
        }

        public int getSize() {
            return queue.size();
        }

        public void addFetchItem(FetchItem item) {
            if (item == null) {
                return;
            }
            queue.add(item);
            totalSize.incrementAndGet();
        }

        public synchronized FetchItem getFetchItem() {
            if (queue.size() == 0) {
                return null;
            }
            return queue.remove(0);
        }

        public synchronized void dump() {
            for (int i = 0; i < queue.size(); i++) {
                FetchItem it = queue.get(i);
            }
        }

    }

    public static class QueueFeeder extends Thread {

        public FetchQueue queue;
        public Generator generator;
        public int size;

        public QueueFeeder(FetchQueue queue, Generator generator, int size) {
            this.queue = queue;
            this.generator = generator;
            this.size = size;
        }

        @Override
        public void run() {
            boolean hasMore = true;
            while (hasMore) {
                int feed = size - queue.getSize();
                if (feed <= 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) { }
                    continue;
                }
                while (feed > 0 && hasMore) {
                    CrawlDatum datum = generator.next();
                    hasMore = (datum != null);
                    if (hasMore) {
                        queue.addFetchItem(new FetchItem(datum));
                        feed--;
                    }
                }
            }
        }

    }

    private class FetcherThread extends Thread {

        @Override
        public void run() {
            activeThreads.incrementAndGet();
            FetchItem item = null;

            try {
                while(true) {
                    try {
                        item = fetchQueue.getFetchItem();
                        if (item == null) {
                            if (feeder.isAlive() || fetchQueue.getSize() > 0) {
                                spinWaiting.incrementAndGet();
                                try {
                                    Thread.sleep(500);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                spinWaiting.decrementAndGet();
                                continue;
                            }
                            else {
                                return;
                            }
                        }

                        lastRequestStart.set(System.currentTimeMillis());

                        CrawlDatum crawldatum = new CrawlDatum();
                        String url = item.datum.getUrl();
                        crawldatum.setUrl(url);

                        Request request = requestFactory.createRequest(url);
                        Response response = null;

                        for (int i = 0; i <= retry; i++) {
                            if (i > 0) {
                            }
                            try {
                                response = request.getResponse(crawldatum);
                                break;
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        crawldatum.setStatus(CrawlDatum.STATUS_DB_FETCHED);
                        crawldatum.setFetchTime(System.currentTimeMillis());

                        Page page = new Page();
                        page.setUrl(url);
                        page.setFetchTime(crawldatum.getFetchTime());

                        if (response == null) {
                            HandlerUtils.sendMessage(handler, new Message(Fetcher.FETCH_FAILED, page), true);
                            continue;
                        }

                        page.setResponse(response);

                        String contentType = response.getContentType();

                        if (parsing) {
                            try {
                                Parser parser = parserFactory.createParser(url, contentType);
                                if (parser != null) {
                                    ParseResult parseresult = parser.getParse(page);
                                    page.setParseResult(parseresult);
                                }
                            } catch (Exception ex) {
                            }
                        }

                        if (needUpdateDb) {
                            try {
                                dbUpdater.getSegmentWriter().wrtieFetch(crawldatum);
                                if (isContentStored) {
                                    Content content = new Content();
                                    content.setUrl(url);
                                    if (response.getContent() != null) {
                                        content.setContent(response.getContent());
                                    } else {
                                        content.setContent(new byte[0]);
                                    }
                                    content.setContentType(contentType);
                                    dbUpdater.getSegmentWriter().wrtieContent(content);
                                }
                                if (parsing && page.getParseResult() != null) {
                                    dbUpdater.getSegmentWriter().wrtieParse(page.getParseResult());
                                }
                            } catch (Exception ex) {
                            }
                        }
                        HandlerUtils.sendMessage(handler, new Message(Fetcher.FETCH_SUCCESS, page), true);
                    } catch (Exception ex) {
                    }
                }
            } catch (Exception ex) {

            } finally {
                activeThreads.decrementAndGet();
            }
        }
    }


    private void before() throws Exception {
        if (needUpdateDb) {
            try {
                if (dbUpdater.isLocked()) {
                    dbUpdater.merge();
                    dbUpdater.unlock();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            dbUpdater.initSegmentWriter();
            dbUpdater.lock();
        }
        running = true;
    }

    /**
     * 抓取当前所有任务，会阻塞到爬取完成
     * @param generator 给抓取提供任务的Generator(抓取任务生成器)
     * @throws Exception
     */
    public void fetchAll(Generator generator) throws Exception {
        before();

        lastRequestStart = new AtomicLong(System.currentTimeMillis());

        activeThreads = new AtomicInteger();
        spinWaiting = new AtomicInteger();
        fetchQueue = new FetchQueue();
        feeder = new QueueFeeder(fetchQueue, generator, 1000);
        feeder.start();

        FetcherThread[] fetcherThreads = new FetcherThread[threads];
        for (int i = 0; i < threads; i++) {
            fetcherThreads[i] = new FetcherThread();
            fetcherThreads[i].start();
        }

        do {
            Thread.sleep(1000);

            if (!feeder.isAlive() && fetchQueue.getSize() < 5) {
                fetchQueue.dump();
            }

            if ((System.currentTimeMillis() - lastRequestStart.get()) > Config.requestMaxInterval) {
                break;
            }
        } while (activeThreads.get() > 0 && running);

        for(int i=0; i<threads; i++) {
            if(fetcherThreads[i].isAlive()) {
                fetcherThreads[i] = null;
            }
        }
        feeder = null;
        fetchQueue.clear();
        after();
    }

    boolean running;

    /**
     * 停止爬取
     */
    public void stop() {
        running = false;
    }

    private void after() throws Exception {
        if (needUpdateDb) {
            dbUpdater.close();
            dbUpdater.merge();
            dbUpdater.unlock();
        }
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public boolean getNeedUpdateDb() {
        return needUpdateDb;
    }

    public void setNeedUpdateDb(boolean needUpdateDb) {
        this.needUpdateDb = needUpdateDb;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public boolean isIsContentStored() {
        return isContentStored;
    }

    public void setIsContentStored(boolean isContentStored) {
        this.isContentStored = isContentStored;
    }

    public boolean isParsing() {
        return parsing;
    }

    public void setParsing(boolean parsing) {
        this.parsing = parsing;
    }

    public DbUpdater getDbUpdater() {
        return dbUpdater;
    }

    public void setDbUpdater(DbUpdater dbUpdater) {
        this.dbUpdater = dbUpdater;
    }

    public RequestFactory getRequestFactory() {
        return requestFactory;
    }

    public void setRequestFactory(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    public ParserFactory getParserFactory() {
        return parserFactory;
    }

    public void setParserFactory(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }
}
