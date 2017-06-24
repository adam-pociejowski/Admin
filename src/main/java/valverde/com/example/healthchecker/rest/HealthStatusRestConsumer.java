package valverde.com.example.healthchecker.rest;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import valverde.com.example.healthchecker.dto.AppHealthDTO;
import valverde.com.example.healthchecker.dto.ApplicationDTO;
import valverde.com.example.healthchecker.dto.HealthDTO;
import valverde.com.example.healthchecker.entity.Application;
import valverde.com.example.healthchecker.enums.HealthState;
import valverde.com.example.healthchecker.service.ApplicationService;

import java.util.ArrayList;
import java.util.Arrays;

@Service
@CommonsLog
public class HealthStatusRestConsumer {

    public AppHealthDTO getHealthStatus(Application app) {
        AppHealthDTO appHealthDTO = null;
        for (int i = 0; i < CHECK_ATTEMPTS; i++) {
            try {
                final String STATUS_HEALTH_POINT = "/health/status";
                String url = app.getHost() + STATUS_HEALTH_POINT;
                ResponseEntity<HealthDTO> response = getRestTemplate().getForEntity(url, HealthDTO.class);
                return getAppHealthDTO(response.getBody());
            } catch (ResourceAccessException e) {
                appHealthDTO = createErrorReport(app, e, new String[] {"Connection timeout "+TIMEOUT+" millis excess."});
            } catch (Exception e) {
                appHealthDTO = createErrorReport(app, e, new String[] {});
            }
        }
        return appHealthDTO;
    }

    private AppHealthDTO getAppHealthDTO(HealthDTO healthDTO) {
        AppHealthDTO appHealthDTO = new AppHealthDTO();
        appHealthDTO.setState(healthDTO.getState());
        appHealthDTO.setMessages(healthDTO.getMessages());
        appHealthDTO.setStats(healthDTO.getStats());
        appHealthDTO.setApplication(new ApplicationDTO(applicationService.findByName(healthDTO.getAppName())));
        return appHealthDTO;
    }

    public void wakeUpApp() {
        try {
            getRestTemplate().getForObject(APP_URL+"/healthchecker/controller/wakeup", String.class);
            log.info("Application awoken.");
        } catch (Exception e) {
            log.error("Problem while trying to wake up application.", e);
        }
    }

    private AppHealthDTO createErrorReport(Application app, Exception e, String[] messages) {
        AppHealthDTO appHealthDTO = new AppHealthDTO();
        appHealthDTO.setApplication(new ApplicationDTO(app));
        appHealthDTO.setState(HealthState.ERROR);
        appHealthDTO.setMessages(Arrays.asList(messages));
        appHealthDTO.setStats(new ArrayList<>());
        log.error("Problem while getting health status from: "+app.getName(), e);
        return appHealthDTO;
    }

    private RestTemplate getRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(TIMEOUT);
        httpRequestFactory.setReadTimeout(TIMEOUT);
        return new RestTemplate(httpRequestFactory);
    }

    @Autowired
    public HealthStatusRestConsumer(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    private final ApplicationService applicationService;

    @Value("${healthchecker.timeout}")
    private Integer TIMEOUT;

    @Value("${application.url}")
    private String APP_URL;

    private static final Integer CHECK_ATTEMPTS = 2;
}