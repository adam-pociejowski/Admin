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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import static valverde.com.example.healthchecker.util.HealthReportUtils.*;

@Service
@CommonsLog
@Transactional
public class HealthCheckerService {

    public List<HealthDTO> getHealthStatusesFromApps() {
        List<HealthDTO> healthDTOs = new ArrayList<>();
        EnumSet.allOf(App.class).forEach(app -> {
            HealthDTO dto = restService.getHealthStatus(app);
            dto.setAppHost(app.getHost());
            healthDTOs.add(dto);
        });
        return healthDTOs;
    }

    public HealthReportDTO getLastHealthReportAsDTO() {
        HealthReport report = repository.findTop1ByOrderByIdDesc();
        HealthReportDTO dto = new HealthReportDTO();
        dto.setReportDate(report.getReportDate());
        dto.setAppReports(convertReportToDTOs(report));
        return dto;
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

    @Autowired
    public HealthCheckerService(HealthReportRepository repository, HealthCheckerRestService restService) {
        this.repository = repository;
        this.restService = restService;
    }

    private final HealthReportRepository repository;

    private final HealthCheckerRestService restService;
}