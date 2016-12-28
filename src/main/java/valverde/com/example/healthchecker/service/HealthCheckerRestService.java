package valverde.com.example.healthchecker.service;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import valverde.com.example.healthchecker.dto.HealthDTO;
import valverde.com.example.healthchecker.enums.App;
import valverde.com.example.healthchecker.enums.HealthState;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Service
@CommonsLog
public class HealthCheckerRestService {

    HealthDTO getHealthStatus(App app) {
        try {
            final String STATUS_HEALTH_POINT = "/health/status";
            String url = app.getHost() + STATUS_HEALTH_POINT;
            ResponseEntity<HealthDTO> response =  new RestTemplate().getForEntity(url, HealthDTO.class);
            return response.getBody();
        } catch (Exception e) {
            HealthDTO result = new HealthDTO();
            result.setAppName(app.getName());
            result.setState(HealthState.ERROR);
            return result;
        }
    }

    public List<App> getApps() {
        return new ArrayList<>(EnumSet.allOf(App.class));
    }
}