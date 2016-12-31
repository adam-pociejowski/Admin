package valverde.com.example.healthchecker.service;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import valverde.com.example.healthchecker.dto.HealthDTO;
import valverde.com.example.healthchecker.dto.HealthReportDTO;
import valverde.com.example.healthchecker.entity.HealthReport;
import valverde.com.example.healthchecker.enums.App;
import valverde.com.example.healthchecker.repository.HealthReportRepository;
import valverde.com.example.healthchecker.rest.HealthStatusRestConsumer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import static valverde.com.example.healthchecker.util.HealthReportUtils.*;

@Service
@CommonsLog
@Transactional
public class HealthCheckerService {

    public List<HealthReportDTO> getAllHealthReports() {
        List<HealthReportDTO> dtos = new ArrayList<>();
        List<HealthReport> reports = repository.findAllByOrderByIdDesc();
        reports.forEach(report -> dtos.add(createHealthReportDTO(report)));
        return dtos;
    }

    public List<HealthDTO> getHealthStatusesFromApps() {
        List<HealthDTO> healthDTOs = new ArrayList<>();
        EnumSet.allOf(App.class).forEach(app -> {
            HealthDTO dto = restConsumer.getHealthStatus(app);
            dto.setAppHost(app.getHost());
            healthDTOs.add(dto);
        });
        return healthDTOs;
    }

    public HealthReportDTO getLastHealthReportAsDTO() {
        return createHealthReportDTO(repository.findTop1ByOrderByIdDesc());
    }

    public void saveReportsFromDTOs(List<HealthDTO> dtos) {
        try {
            HealthReport report = convertToReportFromDTO(dtos);
            report.setStatus(getOverallStatus(dtos));
            repository.save(report);
            log.info("HealthChecker report saved.");
        } catch (Exception e) {
            log.error("Problem while saving healthChecker report.", e);
        }
    }

    public List<App> getApps() {
        return new ArrayList<>(EnumSet.allOf(App.class));
    }

    @Autowired
    public HealthCheckerService(HealthReportRepository repository, HealthStatusRestConsumer restConsumer) {
        this.repository = repository;
        this.restConsumer = restConsumer;
    }

    private final HealthReportRepository repository;

    private final HealthStatusRestConsumer restConsumer;
}