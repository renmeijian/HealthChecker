package com.iflytek.rpa.registryServer.executor;

import com.iflytek.rpa.registryServer.executor.NameThreadFactory;

import java.util.concurrent.*;

import static com.iflytek.rpa.registryServer.core.constant.Constances.*;

public class GlobalExecutor {



    public static void submitDistroNotifyTask(Runnable runnable) {
        DISTRO_NOTIFY_EXECUTOR.submit(runnable);
    }

    private static final ScheduledExecutorService DISTRO_NOTIFY_EXECUTOR =
            newSingleExecutorService(new NameThreadFactory(THREAD_CONSISTENCY));

    public static ScheduledExecutorService newSingleExecutorService(final ThreadFactory threadFactory) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, threadFactory);
        return executorService;
    }

    public static final int DEFAULT_THREAD_COUNT =
            Runtime.getRuntime().availableProcessors() <= 1 ? 1 : Runtime.getRuntime().availableProcessors() / 2;


    public static ScheduledFuture<?> scheduleNamingHealth(Runnable command, long delay, TimeUnit unit) {
        return NAMING_HEALTH_EXECUTOR.schedule(command, delay, unit);
    }

    private static final ScheduledExecutorService NAMING_HEALTH_EXECUTOR =
            Executors.newScheduledThreadPool(Integer.max(DEFAULT_THREAD_COUNT, 1), new NameThreadFactory(THREAD_HEALTHY));

    public static ScheduledFuture<?> scheduleNamingHealth(Runnable command, long initialDelay, long delay,
                                                          TimeUnit unit) {
        return NAMING_HEALTH_EXECUTOR.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }


    public static ExecutorService newFixedExecutorService( final int nThreads,
                                                           final ThreadFactory threadFactory) {
        return Executors.newFixedThreadPool(nThreads, threadFactory);
    }
    private static final ExecutorService CHECK_EXECUTOR = newFixedExecutorService(DEFAULT_THREAD_COUNT,
                    new NameThreadFactory(THREAD_CHECKER));

    public static void executeCheckTask(Runnable runnable) {
        CHECK_EXECUTOR.execute(runnable);
    }

}
