package valverde.com.example.sms.web.dto;

import lombok.Data;

@Data
public class Sms {

    private final String to;

    private final String message;
}