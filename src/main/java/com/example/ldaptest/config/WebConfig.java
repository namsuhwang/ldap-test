package com.example.ldaptest.config;

import com.example.ldaptest.config.intercepter.AccessControlInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@ComponentScan(basePackages = {"com.example.ldaptest"}, excludeFilters = @ComponentScan.Filter(Configuration.class))
// @EnableLdapRepositories(basePackages = "com.example.ldaptest.repository.ldap")
public class WebConfig implements WebMvcConfigurer {
    private final AccessControlInterceptor accessControlInterceptor;

    /*@Bean
    public FilterRegistrationBean filterRegistrationBean() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(new ApiFilter());
        return registrationBean;
    }*/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(accessControlInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/ldap/*", "/css/**", "*.ico", "/error", "/error-page/**");
        // .addPathPatterns("/url1", "/url2");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }


}