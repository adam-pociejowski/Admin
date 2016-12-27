package valverde.com.example.healthchecker.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import valverde.com.example.healthchecker.dto.HealthDTO;
import valverde.com.example.healthchecker.enums.App;
import valverde.com.example.healthchecker.service.HealthCheckerRestService;
import valverde.com.example.healthchecker.service.HealthReportService;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Component
public class HealthCheckerTask {

    @Scheduled(fixedRate = 300000)
    public void checkAppsHealth() throws Exception {
        List<HealthDTO> healthDTOs = new ArrayList<>();
        EnumSet.allOf(App.class).forEach(app -> healthDTOs.add(healthCheckerRestService.getHealthStatus(app)));
        sendToWebSocket(healthDTOs);
        healthReportService.saveReportsFromDTOs(healthDTOs);
    }

    private void sendToWebSocket(List<HealthDTO> results) {
        template.convertAndSend("/topic/greetings", results);
    }

    @Autowired
    public HealthCheckerTask(HealthCheckerRestService healthCheckerRestService,
                             SimpMessagingTemplate template,
                             HealthReportService healthReportService) {
        this.healthCheckerRestService = healthCheckerRestService;
        this.template = template;
        this.healthReportService = healthReportService;
    }

    private HealthCheckerRestService healthCheckerRestService;

    private SimpMessagingTemplate template;

    private HealthReportService healthReportService;
}