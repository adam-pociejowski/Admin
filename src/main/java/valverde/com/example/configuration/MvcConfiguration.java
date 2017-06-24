package valverde.com.example.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("healthchecker.html").setViewName("healthchecker");
        registry.addViewController("health-history.html").setViewName("health-history");
        registry.addViewController("login.html").setViewName("login");
        registry.addViewController("sms-sender.html").setViewName("sms-sender");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/templates/*").addResourceLocations("/resources/templates/");
    }
}