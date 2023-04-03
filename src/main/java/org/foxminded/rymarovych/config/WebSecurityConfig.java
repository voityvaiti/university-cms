package org.foxminded.rymarovych.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    DataSource dataSource;

    @Autowired
    public WebSecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDetailsManager authenticateUsers() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT username, password, enabled FROM usr WHERE username = ?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, role FROM usr JOIN " +
                "users_roles ON usr.id = users_roles.user_id WHERE username = ?");
        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/menu/**").authenticated()

                .requestMatchers("/courses/group-relation/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/courses/teacher-relation/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/courses/new/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/courses/edit/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/courses/delete/**").hasAnyAuthority("ADMIN")
                .requestMatchers("/courses/**").authenticated()

                .requestMatchers("/teachers/courses-relation/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/teachers/new/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/teachers/edit/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/teachers/delete/**").hasAnyAuthority("ADMIN")
                .requestMatchers("/teachers/**").authenticated()
                
                .requestMatchers("/students/new/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/students/edit/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/students/delete/**").hasAnyAuthority("ADMIN")
                .requestMatchers("/students/**").authenticated()

                .requestMatchers("/groups/course-relation/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/groups/new/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/groups/edit/**").hasAnyAuthority("ADMIN", "STUFF")
                .requestMatchers("/groups/delete/**").hasAnyAuthority("ADMIN")
                .requestMatchers("/groups/**").authenticated()

                .requestMatchers("/users/current").authenticated()
                .requestMatchers("/users/**").hasAuthority("ADMIN")
                .anyRequest().permitAll()
        ).formLogin().and().build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


}
