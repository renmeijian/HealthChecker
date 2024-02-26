package com.iflytek.rpa.registryServer.core.healthcheck.processor;

import com.iflytek.rpa.registryServer.core.healthcheck.HealthCheckTask;

public interface HealthCheckProcessor {
    /**
     * Run check task for service.
     *
     * @param task check task
     */
    void process(HealthCheckTask task);

    /**
     * Get check task type, refer to enum HealthCheckType.
     *
     * @return check type
     */
    String getType();
}
