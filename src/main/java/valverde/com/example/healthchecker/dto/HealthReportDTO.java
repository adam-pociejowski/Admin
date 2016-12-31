package valverde.com.example.healthchecker.dto;

import lombok.Data;
import valverde.com.example.healthchecker.enums.HealthState;

import java.util.Date;
import java.util.List;

@Data
public class HealthReportDTO {

    private Date reportDate;

    private HealthState state;

    private List<HealthDTO> appReports;

}