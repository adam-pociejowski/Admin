package valverde.com.example.healthchecker.rest;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import valverde.com.example.healthchecker.enums.App;
import valverde.com.example.healthchecker.service.HealthCheckerRestService;
import java.util.List;

@RestController
@CommonsLog
@RequestMapping("/healthchecker/rest")
public class HealthCheckerRestController {

    private final HealthCheckerRestService healthCheckerRestService;

    @Autowired
    public HealthCheckerRestController(HealthCheckerRestService healthCheckerRestService) {
        this.healthCheckerRestService = healthCheckerRestService;
    }

    @GetMapping("/getapps")
    public ResponseEntity<List<App>> getApps() {
        try {
            List<App> apps = healthCheckerRestService.getApps();
            return new ResponseEntity<>(apps, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problem while getting apps.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}