package valverde.com.example.healthchecker.task;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@CommonsLog
public class TaskTriggers {

    /** Every 10 minutes */
    @Scheduled(cron = "0 0/10 * * * *")
    public void triggerHealthCheckerTask() {
        try {
            healthCheckerTask.startTask();
        } catch (Exception e) {
            log.error("Exception during HealthCheckerTask", e);
        }
    }

    /** Every hour */
    @Scheduled(cron = "0 0 * * * *")
    public void triggerDeleteRedundantRowsTask() {
        try {
            databaseRowLimitTask.startTask();
        } catch (Exception e) {
            log.error("Exception during DeleteRedundantRowsTask", e);
        }
    }

    @Autowired
    public TaskTriggers(HealthCheckerTask healthCheckerTask, DatabaseRowLimitTask databaseRowLimitTask) {
        this.healthCheckerTask = healthCheckerTask;
        this.databaseRowLimitTask = databaseRowLimitTask;
    }

    private HealthCheckerTask healthCheckerTask;

    private DatabaseRowLimitTask databaseRowLimitTask;
}