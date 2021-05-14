package by.tms.Diplom.Configuration;

import by.tms.Diplom.Listener.Listener;
import by.tms.Diplom.Service.CustomUserDetailsService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpSessionListener;

@EnableWebSecurity
@EnableWebMvc
@Configuration
@Slf4j
@ComponentScan(basePackages = "by.tms.Diplom")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        log.info("webSecurityConfig, configure(AuthenticationManagerBuilder auth) - success");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/user/registration", "/user/authentication", "/item/items/**", "/category/categories/**", "/user/addFirstAdmin").anonymous()
                .antMatchers("/", "/item/items/**", "/category/categories/**", "/basket/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/user/authentication")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**")
                .and()
                .headers().frameOptions().sameOrigin();
        log.info("webSecurityConfig, configure(HttpSecurity http) - success");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        log.info("webSecurityConfig, passwordEncoder - success");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(){
        log.info("webSecurityConfig, restTemplate - success");
        return new RestTemplate();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> listenerRegistrationBean(){
        log.info("webSecurityConfig, listenerRegistrationBean - success");
        return new ServletListenerRegistrationBean<>(new Listener());
    }
    @Bean
    public Cloudinary cloudinary(){
        log.info("webSecurityConfig, cloudinary - success");
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "arhitektor",
                "api_key", "891835418633656",
                "api_secret", "9fmlJ3gJvHILi2OJjnBUBrucWa4"));
    }

}