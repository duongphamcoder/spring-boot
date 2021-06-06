package com.example.loginapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WedServiceConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    DataSource dataSource;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/", "/login", "/signup", "/logout").permitAll()
                .antMatchers("/success", "/addnote", "/listnote", "/setting", "/listnote={id}",  "/listnote/searchName={name}")
                .access("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
                .antMatchers("/admin","/admin/memberName={name}",
                        "/admin/searchName={name}").access("hasRole('ROLE_ADMIN')")
                .and().exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests().and().formLogin().loginProcessingUrl("/j_spring_security_check").loginPage("/login")
                .usernameParameter("username").passwordParameter("password").defaultSuccessUrl("/")
                .failureUrl("/login?error=true").and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }
}
