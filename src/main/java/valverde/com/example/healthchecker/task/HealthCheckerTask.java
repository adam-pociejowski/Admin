package valverde.com.example.healthchecker.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import valverde.com.example.healthchecker.dto.AppHealthDTO;
import valverde.com.example.healthchecker.service.HealthCheckerService;

import java.util.List;

@Component
@Transactional
public class HealthCheckerTask extends AbstractTask {

    @Async
    void startTask() {
        logStartTaskInfo();
        List<AppHealthDTO> appHealthDTOS = healthCheckerService.getHealthStatusesFromApps();
        sendToWebSocket(appHealthDTOS);
        healthCheckerService.saveReportsFromDTOs(appHealthDTOS);
        healthCheckerService.wakeUpApp();
        logEndTaskInfo();
    }

    private void sendToWebSocket(List<AppHealthDTO> results) {
        template.convertAndSend("/topic/greetings", results);
    }

    @Autowired
    public HealthCheckerTask(HealthCheckerService healthCheckerService,
                             SimpMessagingTemplate template) {
        this.healthCheckerService = healthCheckerService;
        this.template = template;
    }

    private SimpMessagingTemplate template;

    private final HealthCheckerService healthCheckerService;
}