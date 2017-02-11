package valverde.com.example.healthchecker.task;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import valverde.com.example.healthchecker.entity.HealthAppReport;
import valverde.com.example.healthchecker.entity.HealthReport;
import valverde.com.example.healthchecker.repository.HealthReportRepository;
import java.util.List;

@Component
@CommonsLog
@Transactional
public class DatabaseRowLimitTask extends AbstractTask {

    @Async
    void startTask() {
        logStartTaskInfo();
        List<HealthReport> reports = healthReportRepository.findAllByOrderById();
        Integer rowsAmount = getRowsAmount(reports);
        log.info("Detected "+rowsAmount+" rows in database.");
        if (rowsAmount > ROWS_LIMIT) {
            deletedRows = 0;
            for (HealthReport report : reports) {
                healthReportRepository.delete(report);
                deletedRows += getRowAmountFromReport(report);
                log.info("Deleting report id: "+report.getId()+", reportDate: "+report.getReportDate());
                if (rowsAmount - deletedRows <= ROWS_LIMIT)
                    break;
            }
        }
        logEndTaskInfo();
    }

    @Override
    protected void logEndTaskInfo() {
        log.info(getTaskName()+" finished in "+(System.currentTimeMillis() - startTime)+" millis. " +
                "Deleted "+deletedRows+" rows.");
    }

    private Integer getRowsAmount(List<HealthReport> reports) {
        Integer rowsAmount = 0;
        for (HealthReport report : reports)
            rowsAmount += getRowAmountFromReport(report);
        return rowsAmount;
    }

    private Integer getRowAmountFromReport(HealthReport report) {
        Integer rowsAmount = 1;
        List<HealthAppReport> appReports = report.getAppReports();
        rowsAmount += appReports.size();
        for (HealthAppReport appReport : appReports) {
            rowsAmount += appReport.getMessages().size();
            rowsAmount += appReport.getStats().size();
        }
        return rowsAmount;
    }

    @Autowired
    public DatabaseRowLimitTask(HealthReportRepository healthReportRepository) {
        this.healthReportRepository = healthReportRepository;
    }

    private Integer deletedRows;

    private static final Integer ROWS_LIMIT = 9000;

    private final HealthReportRepository healthReportRepository;
}