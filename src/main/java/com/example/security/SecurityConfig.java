package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/main").hasRole("ADMIN")
        .requestMatchers("/main").authenticated()
            .requestMatchers(
                "/",
                "/login",
                "/vaadinServlet/**",
                "/VAADIN/**",
                "/frontend/**",
                "/webjars/**",
                "/build/**",
                "/sw.js",
                "/manifest.webmanifest"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/main", true) // Force redirect to /main
            .permitAll()
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/login")
            .permitAll()
        )
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/**") // Temporary disable for testing
        )
        .headers(headers -> headers
            .frameOptions().sameOrigin()
        );
    return http.build();
}
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build();
            
        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin")
            .roles("ADMIN")
            .build();
            
        return new InMemoryUserDetailsManager(user, admin);
    }
}