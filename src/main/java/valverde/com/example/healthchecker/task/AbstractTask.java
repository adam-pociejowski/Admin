package valverde.com.example.healthchecker.task;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public abstract class AbstractTask {

    Long startTime;

    abstract void startTask();

    String getTaskName() {
        return "["+getClass().getSimpleName()+"]:";
    }

    void logStartTaskInfo() {
        startTime = System.currentTimeMillis();
        log.info(getTaskName()+" started.");
    }

    protected void logEndTaskInfo() {
        log.info(getTaskName()+" finished in "+(System.currentTimeMillis() - startTime)+" millis.");
    }
}
