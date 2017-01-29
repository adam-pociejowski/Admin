package valverde.com.example.healthchecker.rest;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import valverde.com.example.healthchecker.dto.HealthDTO;
import valverde.com.example.healthchecker.enums.App;
import valverde.com.example.healthchecker.enums.HealthState;
import java.util.Arrays;

@Service
@CommonsLog
public class HealthStatusRestConsumer {

    public HealthDTO getHealthStatus(App app) {
        try {
            final String STATUS_HEALTH_POINT = "/health/status";
            String url = app.getHost() + STATUS_HEALTH_POINT;
            ResponseEntity<HealthDTO> response = getRestTemplate().getForEntity(url, HealthDTO.class);
            return response.getBody();
        } catch (ResourceAccessException e) {
            return createErrorReport(app, e, new String[] {"Connection timeout "+TIMEOUT+" millis excess."});
        } catch (Exception e) {
            return createErrorReport(app, e, new String[] {});
        }
    }

    public void wakeUpApp() {
        try {
            getRestTemplate().getForObject(APP_URL+"/healthchecker/rest/wakeup", String.class);
            log.info("Application awoken.");
        } catch (Exception e) {
            log.error("Problem while trying to wake up application.", e);
        }
    }

    private HealthDTO createErrorReport(App app, Exception e, String[] messages) {
        HealthDTO healthDTO = new HealthDTO();
        healthDTO.setAppName(app.getName());
        healthDTO.setState(HealthState.ERROR);
        healthDTO.setMessages(Arrays.asList(messages));
        log.error("Problem while getting health status from: "+app.getName(), e);
        return healthDTO;
    }

    private RestTemplate getRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(TIMEOUT);
        httpRequestFactory.setReadTimeout(TIMEOUT);
        return new RestTemplate(httpRequestFactory);
    }

    @Value("${healthchecker.timeout}")
    private Integer TIMEOUT;

    @Value("${application.url}")
    private String APP_URL;
}