package com.iflytek.rpa.registryServer.consistency.impl;

import com.iflytek.rpa.registryServer.executor.GlobalExecutor;
import com.iflytek.rpa.registryServer.consistency.ConsistencyService;
import com.iflytek.rpa.registryServer.core.entity.Instances;
import com.iflytek.rpa.registryServer.core.entity.ServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

import static com.iflytek.rpa.registryServer.core.constant.Constances.LOG_ERROR_HANDLE;


@Service
public class ConsistencyServiceImpl implements ConsistencyService {

    private final static Logger log = LoggerFactory.getLogger(ConsistencyServiceImpl.class);

    private volatile Notifier notifier = new Notifier();

    @Autowired
    private ServiceManager serviceManager;

    @PostConstruct
    public void init() {
        GlobalExecutor.submitDistroNotifyTask(notifier);
    }


    @Override
    public void put(Instances instances) {
        notifier.addTask(instances);
    }

    public class Notifier implements Runnable {
        /**
         * 阻塞队列
         */
        public BlockingQueue<Instances> tasks = new ArrayBlockingQueue<>(1024 * 1024);

        /**
         * 添加任务到队列
         */
        public void addTask(Instances instances) {

            tasks.offer(instances);
        }

        @Override
        public void run() {

            for (; ; ) {
                try {
                    Instances instances = tasks.take();
                    serviceManager.handle(instances);
                } catch (Throwable e) {
                    log.error(LOG_ERROR_HANDLE, e);
                }
            }
        }

    }

}
