package com.santosglaiton.tracksystem.config;

import com.santosglaiton.tracksystem.config.jwt.JWTAuthenticationFilter;
import com.santosglaiton.tracksystem.config.jwt.JWTLoginFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure (HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/*").permitAll()
                .antMatchers(HttpMethod.GET, "/*/*").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                //filtra requisições de login
                .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                //filtra outras requisições para verificar a presença do token kwt no header
                .addFilterBefore(new JWTAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);

    }

    //cria uma conta default
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("password")
                .roles("ADMIN");
    }

}
