package com.mengka.springboot.component;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

/**
 * @author huangyy
 * @version cabbage-forward2.0,2018-03-09
 * @since cabbage-forward2.0
 */
public class StatsdExecuterInitialize {

    private static final StatsDClient statsd = new NonBlockingStatsDClient(
            "cabbageforward",                          /* prefix to any stats; may be null or empty string */
            "127.0.0.1",                        /* common case: localhost */
            8125                                 /* port */
    );

    /**
     *  消息计数+1
     *
     * @param var1
     */
    void incrementCounter(String var1) {
        statsd.incrementCounter(var1);
    }

    /**
     *  计数功能
     *
     * @param var1 key
     * @param var2 value
     */
    void count(String var1, long var2){
        statsd.count(var1,var2);
    }

    public static StatsdExecuterInitialize getInitialize() {
        return StatsdExecuterInitialize_HolderHolder.statsdExecuterInitialize_Holder;
    }

    private static class StatsdExecuterInitialize_HolderHolder {
        private static final StatsdExecuterInitialize statsdExecuterInitialize_Holder = new StatsdExecuterInitialize();
    }
}
