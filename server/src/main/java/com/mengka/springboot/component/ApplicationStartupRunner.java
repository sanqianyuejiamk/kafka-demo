package com.mengka.springboot.component;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author huangyy
 * @version cabbage-forward2.0,2018-03-09
 * @since cabbage-forward2.0
 */
@Log4j2
@Order(value = 3)
@Component
public class ApplicationStartupRunner implements CommandLineRunner {

    private static final StatsdExecuterInitialize statsdExecuterInitialize = StatsdExecuterInitialize.getInitialize();

    @Override
    public void run(String... args) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info(">>开始启动Cabbage-Forward服务...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    statsdExecuterInitialize.count("t1", 1);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }).start();


        log.info(">>Cabbage-Forward环境初始化完成.");
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
