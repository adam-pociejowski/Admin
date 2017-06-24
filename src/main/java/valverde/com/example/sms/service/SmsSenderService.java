package valverde.com.example.sms.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import valverde.com.example.sms.web.dto.Sms;
import java.nio.charset.Charset;

@Service
public class SmsSenderService {

    public void send(Sms sms) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", getBasicHeader());
        headers.add("Content-Type", "application/json");

        HttpEntity<?> httpEntity = new HttpEntity<Object>(sms, headers);
        final String SMS_ENDPOINT = "/notifier/rest/sms";
        ResponseEntity<Object> responseEntity = new RestTemplate()
                .exchange(SMS_SERVICE_URL + SMS_ENDPOINT, HttpMethod.POST, httpEntity, Object.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if (httpStatus != HttpStatus.OK) {
            throw new NotSendSmsException("Problem while sending sms httpStatus code: "+httpStatus.value());
        }
    }

    private String getBasicHeader() {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        return  "Basic " + new String(encodedAuth);
    }

    public SmsSenderService(Environment env) {
        SMS_SERVICE_URL = env.getProperty("notifier.service.url");
        username = env.getProperty("notifier.service.username");
        password = env.getProperty("notifier.service.password");
    }

    private final String SMS_SERVICE_URL;

    private final String username;

    private final String password;

    class NotSendSmsException extends RuntimeException {

        NotSendSmsException(String message) {
            super(message);
        }
    }
}