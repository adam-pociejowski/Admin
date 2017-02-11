package valverde.com.example.healthchecker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import valverde.com.example.healthchecker.entity.HealthAppReport;
import valverde.com.example.healthchecker.enums.HealthState;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AppHealthDTO {

    private ApplicationDTO application;

    private HealthState state;

    private List<StatDTO> stats;

    private List<String> messages;

    public AppHealthDTO(HealthAppReport appReport) {
        this.application = new ApplicationDTO(appReport.getApplication());
        this.state = appReport.getStatus();
        stats = new ArrayList<>();
        messages = new ArrayList<>();
        appReport.getStats().forEach(stat -> stats.add(new StatDTO(stat)));
        appReport.getMessages().forEach(message -> messages.add(message.getMessage()));
    }
}