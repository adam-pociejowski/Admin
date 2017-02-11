package valverde.com.example.healthchecker.dto;

import lombok.Data;
import valverde.com.example.healthchecker.entity.HealthReport;
import valverde.com.example.healthchecker.enums.HealthState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class HealthReportDTO {

    private Date reportDate;

    private HealthState state;

    private List<AppHealthDTO> appReports;

    public HealthReportDTO(HealthReport report) {
        this.reportDate = report.getReportDate();
        this.state = report.getStatus();
        appReports = new ArrayList<>();
        report.getAppReports().forEach(appReport -> appReports.add(new AppHealthDTO(appReport)));
    }

}