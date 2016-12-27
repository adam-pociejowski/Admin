package valverde.com.example.healthchecker.service;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import valverde.com.example.healthchecker.dto.HealthDTO;
import valverde.com.example.healthchecker.dto.HealthReportDTO;
import valverde.com.example.healthchecker.dto.StatDTO;
import valverde.com.example.healthchecker.entity.HealthAppReport;
import valverde.com.example.healthchecker.entity.HealthMessage;
import valverde.com.example.healthchecker.entity.HealthReport;
import valverde.com.example.healthchecker.entity.HealthStat;
import valverde.com.example.healthchecker.enums.App;
import valverde.com.example.healthchecker.enums.HealthState;
import valverde.com.example.healthchecker.repository.HealthReportRepository;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;
import static valverde.com.example.healthchecker.enums.HealthState.*;

@Service
@CommonsLog
@Transactional
public class HealthReportService {

    public HealthReportDTO getLastHealthReportAsDTO() {
        HealthReport report = repository.findTop1ByOrderByIdDesc();
        HealthReportDTO dto = new HealthReportDTO();
        dto.setReportDate(report.getReportDate());
        dto.setAppReports(convertReportToDTOs(report));
        return dto;
    }

    public void saveReportsFromDTOs(List<HealthDTO> dtos) {
        try {
            HealthReport report = convertToReport(dtos);
            report.setStatus(getOverallStatus(dtos));
            repository.save(report);
            log.info("HealthChecker report saved.");
        } catch (Exception e) {
            log.error("Problem while saving healthChecker report.", e);
        }
    }

    private List<HealthDTO> convertReportToDTOs(HealthReport report) {
        List<HealthDTO> dtos = new ArrayList<>();
        report.getAppReports().forEach(appReport -> {
            HealthDTO dto = new HealthDTO();
            dto.setState(appReport.getStatus());
            dto.setAppName(appReport.getAppName());
            dto.setMessages(getMessages(appReport));
            dto.setStats(getStats(appReport));
            dto.setAppHost(getAppByName(appReport.getAppName()).getHost());
            dtos.add(dto);
        });
        return dtos;
    }

    private App getAppByName(String appName) {
        for (App app : EnumSet.allOf(App.class)) {
            if (app.getName().equals(appName))
                return app;
        }
        return App.SPORT_CENTER;
    }

    private List<StatDTO> getStats(HealthAppReport appReport) {
        List<StatDTO> stats = new ArrayList<>();
        appReport.getStats().forEach(s -> {
            StatDTO stat = new StatDTO();
            stat.setStat(s.getStat());
            stat.setStatName(s.getStatName());
            stats.add(stat);
        });
        return stats;
    }

    private List<String> getMessages(HealthAppReport appReport) {
        List<String> messages = new ArrayList<>();
        appReport.getMessages().forEach(m -> messages.add(m.getMessage()));
        return messages;
    }

    private HealthState getOverallStatus(List<HealthDTO> dtos) {
        HealthState state = SUCCESS;
        for (HealthDTO dto : dtos) {
            if (dto.getState().equals(WARNING) && !state.equals(ERROR)) {
                state = WARNING;
            } else if (dto.getState().equals(ERROR)) {
                state = ERROR;
            }
        }
        return state;
    }

    private HealthReport convertToReport(List<HealthDTO> dtos) {
        HealthReport report = new HealthReport();
        Date now = new Date(Calendar.getInstance().getTime().getTime());
        report.setReportDate(now);
        List<HealthAppReport> appReports = new ArrayList<>();
        dtos.forEach(dto -> appReports.add(createAppReportFromDTO(report, dto)));
        report.setAppReports(appReports);
        return report;
    }

    private HealthAppReport createAppReportFromDTO(HealthReport report, HealthDTO dto) {
        HealthAppReport appReport = new HealthAppReport();
        appReport.setStatus(dto.getState());
        appReport.setAppName(dto.getAppName());
        appReport.setReport(report);
        appReport.setMessages(createMessagesFromDTO(dto, appReport));
        appReport.setStats(createStatsFromDTO(dto, appReport));
        return appReport;
    }

    private List<HealthStat> createStatsFromDTO(HealthDTO dto, HealthAppReport appReport) {
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

    private List<HealthMessage> createMessagesFromDTO(HealthDTO dto, HealthAppReport appReport) {
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
    public HealthReportService(HealthReportRepository repository) {
        this.repository = repository;
    }

    private final HealthReportRepository repository;
}