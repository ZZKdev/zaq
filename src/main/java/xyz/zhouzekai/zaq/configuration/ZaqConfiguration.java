package xyz.zhouzekai.zaq.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.*;
import xyz.zhouzekai.zaq.interceptor.PassportInterceptor;

@Component
public class ZaqConfiguration implements WebMvcConfigurer{
    @Autowired
    PassportInterceptor passportInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/questions/{id}").setViewName("/question");
    }
}
