package valverde.com.example.healthchecker.service;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import valverde.com.example.healthchecker.dto.*;
import valverde.com.example.healthchecker.entity.*;
import valverde.com.example.healthchecker.enums.HealthState;
import valverde.com.example.healthchecker.repository.HealthReportRepository;
import valverde.com.example.healthchecker.rest.HealthStatusRestConsumer;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static valverde.com.example.healthchecker.util.HealthReportUtils.*;

@Service
@CommonsLog
@Transactional
public class HealthCheckerService {

    public Long getReportAmount() {
        return repository.count();
    }

    public List<HealthReportDTO> getReportsForPage(Pageable pageable) {
        List<HealthReportDTO> dtos = new ArrayList<>();
        List<HealthReport> reports = repository.findAll(pageable).getContent();
        reports.forEach(report -> dtos.add(new HealthReportDTO(report)));
        return dtos;
    }

    public List<AppHealthDTO> getHealthStatusesFromApps() {
        List<AppHealthDTO> appHealthDTOS = new ArrayList<>();
        List<Application> activeApps = applicationService.getActiveApplications();
        activeApps.forEach(app -> appHealthDTOS.add(restConsumer.getHealthStatus(app)));
        return appHealthDTOS;
    }

    public void saveReportsFromDTOs(List<AppHealthDTO> dtos) {
        try {
            HealthReport report = convertToReportFromDTO(dtos);
            report.setStatus(getOverallStatus(dtos));
            repository.save(report);
            log.info("HealthChecker report saved.");
        } catch (Exception e) {
            log.error("Problem while saving healthChecker report.", e);
        }
    }

    public DetailsDTO getDetails() {
        List<Application> applications = applicationService.getAllApplications();
        DetailsDTO details = new DetailsDTO();
        HealthReportDTO report = getLastHealthReportAsDTO();
        details.setReportDate(getReportDate(report));
        details.setDetails(getAppsDetails(applications, report));
        return details;
    }

    private List<ApplicationDetailsDTO> getAppsDetails(List<Application> applications,
                                                       HealthReportDTO report) {
        List<ApplicationDetailsDTO> appsDetails = new ArrayList<>();
        for (Application application : applications) {
            ApplicationDetailsDTO appDetails = new ApplicationDetailsDTO();
            appDetails.setApplication(new ApplicationDTO(application));
            if (application.getActive() == null || !application.getActive()) {
                appDetails.setState(HealthState.INACTIVE);
            } else {
                appDetails.setState(getAppStateFromLastReport(report, application));
            }
            appsDetails.add(appDetails);
        }
        return appsDetails;
    }

    private java.util.Date getReportDate(HealthReportDTO report) {
        return report != null ? report.getReportDate() : new java.util.Date();
    }

    public void wakeUpApp() {
        restConsumer.wakeUpApp();
    }

    private HealthState getAppStateFromLastReport(HealthReportDTO report, Application application) {
        if (report == null)
            return HealthState.NO_DATA;

        for (AppHealthDTO appHealthDTO : report.getAppReports()) {
            if (appHealthDTO.getApplication().getId().equals(application.getId())) {
                return appHealthDTO.getState();
            }
        }
        return HealthState.NO_DATA;
    }

    private HealthReportDTO getLastHealthReportAsDTO() {
        HealthReport report = repository.findTop1ByOrderByIdDesc();
        if (report == null) {
            log.error("No report found.");
            return null;
        }

        return new HealthReportDTO(report);
    }

    private HealthReport convertToReportFromDTO(List<AppHealthDTO> dtos) {
        HealthReport report = new HealthReport();
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        report.setReportDate(now);
        List<HealthAppReport> appReports = new ArrayList<>();
        dtos.forEach(dto -> appReports.add(createAppReportFromDTO(report, dto)));
        report.setAppReports(appReports);
        return report;
    }

    private HealthAppReport createAppReportFromDTO(HealthReport report, AppHealthDTO dto) {
        HealthAppReport appReport = new HealthAppReport();
        appReport.setStatus(dto.getState());
        appReport.setApplication(applicationService.findById(dto.getApplication().getId()));
        appReport.setReport(report);
        appReport.setMessages(createMessagesFromDTO(dto, appReport));
        appReport.setStats(createStatsFromDTO(dto, appReport));
        return appReport;
    }

    private List<HealthStat> createStatsFromDTO(AppHealthDTO dto, HealthAppReport appReport) {
        List<HealthStat> stats = new ArrayList<>();
        dto.getStats().forEach(stat -> {
            HealthStat s = new HealthStat();
            s.setAppReport(appReport);
            s.setStatName(stat.getStatName());
            s.setStat(stat.getStat().toString());
            stats.add(s);
        });
        return stats;
    }

    private List<HealthMessage> createMessagesFromDTO(AppHealthDTO dto, HealthAppReport appReport) {
        List<HealthMessage> messages = new ArrayList<>();
        dto.getMessages().forEach(message -> {
            HealthMessage m = new HealthMessage();
            m.setAppReport(appReport);
            m.setMessage(message);
            messages.add(m);
        });
        return messages;
    }

    @Autowired
    public HealthCheckerService(HealthReportRepository repository, HealthStatusRestConsumer restConsumer,
                                ApplicationService applicationService) {
        this.repository = repository;
        this.restConsumer = restConsumer;
        this.applicationService = applicationService;
    }

    private final HealthReportRepository repository;

    private final HealthStatusRestConsumer restConsumer;

    private final ApplicationService applicationService;
}