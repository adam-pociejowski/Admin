package valverde.com.example.healthchecker.enums;

import lombok.Getter;

public enum App {
    BUS_CHECKER("BusChecker", "http://buschecker.herokuapp.com"),
    SPORT_CENTER("SportCenter", "http://37.139.26.214:8080");

    @Getter
    private String name;

    @Getter
    private String host;

    App(String name, String host) {
        this.name = name;
        this.host = host;
    }
}