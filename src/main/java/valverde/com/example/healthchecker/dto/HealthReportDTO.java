package valverde.com.example.healthchecker.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class HealthReportDTO {

    private Date reportDate;

    private List<HealthDTO> appReports;

}