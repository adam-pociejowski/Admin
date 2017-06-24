package valverde.com.example.login.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = {"/", "/index"})
    public String showMainPage() {
        return "index";
    }
}
