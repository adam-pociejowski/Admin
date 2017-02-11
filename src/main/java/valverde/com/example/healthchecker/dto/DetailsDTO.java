package valverde.com.example.healthchecker.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class DetailsDTO {

    private List<ApplicationDetailsDTO> details;

    private Date reportDate;
}
