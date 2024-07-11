package ru.maxima.springrest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.maxima.springrest.security.JWTFilter;
import ru.maxima.springrest.security.PersonDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;
    private final JWTFilter jwtFilter;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, JWTFilter jwtFilter) {
        this.personDetailsService = personDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(s -> s.requestMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(s  -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

//                .formLogin(s -> s.loginPage("/auth/login")
//                        .loginProcessingUrl("/process_login")
//                        .defaultSuccessUrl("/people", true)
//                        .failureUrl("/auth/login?error"))
//                .logout(s  -> s.logoutUrl("/logout")
//                        .logoutSuccessUrl("/auth/login")
//                );


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(personDetailsService)
//                .passwordEncoder(getPasswordEncoder())
//                .build();

        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(personDetailsService).passwordEncoder(getPasswordEncoder());
        return builder.build();
    }


    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
