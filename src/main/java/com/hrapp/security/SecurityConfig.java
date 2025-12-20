package com.hrapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationSuccessHandler successHandler;

    public SecurityConfig(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/api/**"))
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/error").permitAll()
                .requestMatchers("/css/**", "/js/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                

                .requestMatchers("/homeRH").hasRole("RH")
                .requestMatchers("/homeDRH").hasRole("DRH")
                .requestMatchers("/homeADMIN").hasRole("ADMIN")

                .requestMatchers("/home").authenticated()

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
            )
            .build();
    }
}
