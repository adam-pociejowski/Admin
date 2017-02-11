package valverde.com.example.healthchecker.dto;

import lombok.Data;
import valverde.com.example.healthchecker.enums.HealthState;
import java.util.Date;

@Data
public class ApplicationDetailsDTO {

    private ApplicationDTO application;

    private HealthState state;

    private Date reportDate;

}
