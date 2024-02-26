package com.iflytek.rpa.registryServer.executor;

import com.iflytek.rpa.registryServer.executor.GlobalExecutor;
import com.iflytek.rpa.registryServer.core.healthcheck.ClientBeatCheckTask;
import com.iflytek.rpa.registryServer.core.healthcheck.HealthCheckTask;

import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.iflytek.rpa.registryServer.core.constant.Constances.*;

public class HealthCheckReactor {
    public static ScheduledFuture<?> scheduleNow(Runnable task) {
        return GlobalExecutor.scheduleNamingHealth(task, ZERO_SECOND, TimeUnit.MILLISECONDS);
    }

    public static void scheduleCheck(ClientBeatCheckTask task) {
         GlobalExecutor.scheduleNamingHealth(task, FIVE_SECOND, FIVE_SECOND, TimeUnit.MILLISECONDS);
    }

    public static ScheduledFuture<?> scheduleCheck(HealthCheckTask task) {
        Random random = new Random();
        int checkRtNormalized = TWO_SECOND + random.nextInt(FIVE_SECOND);
        return GlobalExecutor.scheduleNamingHealth(task,  FIVE_SECOND, checkRtNormalized, TimeUnit.MILLISECONDS);
    }
}
