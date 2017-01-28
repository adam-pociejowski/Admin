package valverde.com.example.healthchecker.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import valverde.com.example.healthchecker.dto.HealthDTO;
import valverde.com.example.healthchecker.service.HealthCheckerService;
import java.util.List;

@Component
public class HealthCheckerTask {

    @Scheduled(fixedRate = 600000)
    public void checkAppsHealth() throws Exception {
        healthCheckerService.wakeUpApp();
        List<HealthDTO> healthDTOs = healthCheckerService.getHealthStatusesFromApps();
        sendToWebSocket(healthDTOs);
        healthCheckerService.saveReportsFromDTOs(healthDTOs);
    }

    private void sendToWebSocket(List<HealthDTO> results) {
        template.convertAndSend("/topic/greetings", results);
    }


    @Autowired
    public HealthCheckerTask(SimpMessagingTemplate template, HealthCheckerService healthCheckerService) {
        this.template = template;
        this.healthCheckerService = healthCheckerService;
    }

    private SimpMessagingTemplate template;

    private HealthCheckerService healthCheckerService;
}