package org.example.config;

import org.example.utils.MD5Util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Component
@Configuration
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(CustomUserDetailService customUserDetailService, JwtTokenFilter jwtTokenFilter) {
        this.customUserDetailService = customUserDetailService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // authentication
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry

                    .requestMatchers("/v2/api-docs").permitAll()
                    .requestMatchers("/v3/api-docs").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/swagger-resources").permitAll()
                    .requestMatchers("/swagger-resources/**").permitAll()
                    .requestMatchers("/configuration/ui").permitAll()
                    .requestMatchers("/configuration/security").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/webjars/**").permitAll()
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/auth/registration").permitAll()
                    .requestMatchers(("/auth/verification/**")).permitAll()
                    .requestMatchers("/auth/login").permitAll()
                    .requestMatchers("/auth/registration").permitAll()
                    .requestMatchers("/tag/any/**").permitAll()
                    .anyRequest()
                    .authenticated();
        });
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { //
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return MD5Util.getMD5(rawPassword.toString()).equals(encodedPassword);
            }
        };
    }
}
