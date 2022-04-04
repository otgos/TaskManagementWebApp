package com.taskmanager.upper.config;

import com.taskmanager.upper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedPage("/accessError");
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**").permitAll();
        http.formLogin()
                .loginProcessingUrl("/user/toEnter").permitAll() //<--where to send post request to sign in
                .loginPage("/user/signIn").permitAll()//<-- default page to sign in
                .usernameParameter("user_mail") //<--imput email type "email"
                .passwordParameter("user_password") //<-- imput type "password
                .defaultSuccessUrl("/user/profile") //<where to redirect after successfull signin
                .failureUrl("/user/signIn?error");  //<-- where to redirect after unsuccessful signin
        http.logout()
                .logoutSuccessUrl("/user/signIn") //<-- after click of logout where to redirect
                .logoutUrl("/toExit");  //<-- url end point to sing out
        http.csrf().disable();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
