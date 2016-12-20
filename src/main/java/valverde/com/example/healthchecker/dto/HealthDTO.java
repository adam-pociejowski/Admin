package valverde.com.example.healthchecker.dto;

import lombok.Data;
import valverde.com.example.healthchecker.enums.HealthState;

@Data
public class HealthDTO {

    private HealthState state;

}