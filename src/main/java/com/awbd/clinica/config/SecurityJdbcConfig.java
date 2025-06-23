package com.awbd.clinica.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import org.springframework.http.HttpMethod;

import java.sql.SQLException;

@Configuration
public class SecurityJdbcConfig {

    @Autowired
    private com.awbd.clinica.service.security.JpaUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ADMIN only – modificări
                        .requestMatchers(HttpMethod.POST, "/medications/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/medications/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/medications/**").hasRole("ADMIN")
                        .requestMatchers("/patients/new").hasRole("ADMIN")
                        .requestMatchers("/appointments/new").hasRole("ADMIN")

                        // ADMIN + GUEST – vizualizări
                        .requestMatchers("/clinics/**").hasAnyRole("ADMIN", "GUEST")
                        .requestMatchers("/doctors/**").hasAnyRole("ADMIN", "GUEST")
                        .requestMatchers("/appointments/**").hasAnyRole("ADMIN", "GUEST")
                        .requestMatchers("/patients/info/**").hasAnyRole("ADMIN", "GUEST")
                        .requestMatchers("/patients").hasAnyRole("ADMIN", "GUEST")
                        // Orice altceva necesită login
                        .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/login")                // pagină proprie de login
                        .loginProcessingUrl("/authUser")        // ruta POST care procesează loginul
                        .defaultSuccessUrl("/", true)           // unde se duce după login
                        .failureUrl("/login?error=true")    // în caz de parolă greșită
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}

