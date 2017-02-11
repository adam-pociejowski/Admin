package valverde.com.example.healthchecker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import valverde.com.example.healthchecker.entity.Application;

@Data
@NoArgsConstructor
public class ApplicationDTO {

    private Long id;

    private String name;

    private String host;

    private Boolean active;

    public ApplicationDTO(Application application) {
        this.id = application.getId();
        this.name = application.getName();
        this.host = application.getHost();
        this.active = application.getActive();
    }
}
