package valverde.com.example.configuration;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import valverde.com.example.login.entity.User;
import valverde.com.example.login.service.UserService;
import java.util.ArrayList;
import java.util.List;

@Component
@CommonsLog
public class AuthProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            User user = userService.findByUsername(username);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("USER_ROLE"));
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            }
        } catch (Exception e) {
            log.info("Wrong password for username "+username);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Autowired
    public AuthProvider(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;
}