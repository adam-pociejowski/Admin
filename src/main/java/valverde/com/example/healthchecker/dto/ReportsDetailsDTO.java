package valverde.com.example.healthchecker.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ReportsDetailsDTO {

    private Date lastSuccessDate;

    private Date lastErrorDate;

    private Long reportsAmount;
}