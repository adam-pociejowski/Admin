package valverde.com.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/bootstrap-datepicker-dist/**", "/js/**", "/css/**",
                "/angular-1.5.8/**", "/bootstrap-3.3.7-dist/**", "./templates/*", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(restEntryAuthenticationPoint)
                .and()
                .formLogin()
                .failureHandler(authFailureHandler)
                .and()
                .logout()
                .logoutSuccessUrl("/#/login")
                .and()
                .authorizeRequests()
                .antMatchers("/**", "/login.html", "/healthchecker.html",
                        "/healthchecker/**", "/gs-guide-websocket/**")
                .permitAll()
                .anyRequest()
                .authenticated();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Autowired
    public WebSecurityConfiguration(AuthProvider authProvider,
                                    RestEntryAuthenticationPoint restEntryAuthenticationPoint,
                                    AuthFailureHandler authFailureHandler) {
        this.authProvider = authProvider;
        this.restEntryAuthenticationPoint = restEntryAuthenticationPoint;
        this.authFailureHandler = authFailureHandler;
    }

    private final AuthProvider authProvider;

    private final RestEntryAuthenticationPoint restEntryAuthenticationPoint;

    private final AuthFailureHandler authFailureHandler;
}