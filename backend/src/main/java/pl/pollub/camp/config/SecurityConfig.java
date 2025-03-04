package pl.pollub.camp.config;

import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.pollub.camp.Models.Role;
import pl.pollub.camp.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter authfilter;
    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/auth/**",
            "/reservation/find",
            "/vehicle/all",
            "/vehicle/id/**",
            "/reservation/vehicle/**",
            "/prices/find",
            "/location/all"
    };// lista dozwolonych enpointów dla niezalogowanych

    private static final String[] CLIENT_ENDPONTS = {
            "/reservation",
            "/reservation/**",
            "/reservation/resign/**",
            "/location/all"
    };

    private static final String[] EMPLOYEE_ENDPOINTS = {
            "/vehicle/add",
            "/vehicle/update/**",
            "/vehicle-type/**",
            "/user/all",
            "/reservation/all",
            "/reservation/cancel/**",
            "/reservation/accept/**",
            "/prices/add",
            "/prices/update",
            "/prices/all",
            "/princes/delete",
            "/order/**",
            "/inspection/**",
            "/location/all",
            "/reports",
            "/reports/**",
            "/repairs",
            "/repairs/**"
    };//  lista endpointów dla admina


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(CLIENT_ENDPONTS).hasAnyRole(Role.CUSTOMER.toString(), Role.EMPLOYEE.toString(),Role.ADMIN.toString())
                        .requestMatchers(EMPLOYEE_ENDPOINTS).hasAnyRole(Role.EMPLOYEE.toString(), Role.ADMIN.toString())
                        .requestMatchers("/**").hasAnyRole(Role.ADMIN.toString())
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                )
                .addFilterBefore(authfilter, UsernamePasswordAuthenticationFilter.class)// Add JWT filter
                .cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}