package valverde.com.example.healthchecker.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import valverde.com.example.healthchecker.dto.HealthDTO;
import valverde.com.example.healthchecker.enums.App;
import valverde.com.example.healthchecker.service.HealthCheckerRestService;

@Component
public class HealthCheckerTask {

    private final HealthCheckerRestService healthCheckerRestService;

    @Autowired
    public HealthCheckerTask(HealthCheckerRestService healthCheckerRestService) {
        this.healthCheckerRestService = healthCheckerRestService;
    }

    @Scheduled(fixedRate = 20000)
    public void checkAppsHealth() {
        HealthDTO healthDTO = healthCheckerRestService.getHealthStatus(App.BUS_CHECKER.getHost());
        System.out.println(healthDTO.toString());
    }
}