package valverde.com.example.sms.web.rest;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import valverde.com.example.sms.service.SmsSenderService;
import valverde.com.example.sms.web.dto.Sms;

@CommonsLog
@RestController
@RequestMapping("/sms")
public class SmsSenderController {

    @PostMapping("/send")
    @PreAuthorize("hasAuthority('USER_ROLE')")
    public HttpStatus sendSms(@RequestBody Sms sms) {
        try {
            smsSenderService.send(sms);
            log.info("Sms sent to "+sms.getTo());
        } catch (Exception e) {
            log.error("Problem while sending sms.", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.OK;
    }

    public SmsSenderController(SmsSenderService smsSenderService) {
        this.smsSenderService = smsSenderService;
    }

    private final SmsSenderService smsSenderService;
}
