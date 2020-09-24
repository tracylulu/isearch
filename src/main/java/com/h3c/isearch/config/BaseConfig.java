package com.h3c.isearch.config;

import com.h3c.isearch.interceptor.SsoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: isearch-base
 * @description:
 * @author: zhanghao
 * @create: 2020-05-25 09:11
 **/
@Configuration
public class BaseConfig {

    //增加视图跳转
    @Bean
    public WebMvcConfigurer viewResolver(){
        return new WebMvcConfigurer(){

            @Override
            public void addViewControllers(ViewControllerRegistry registry){
                registry.addViewController("/").setViewName("view/globalQueryView");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new SsoInterceptor()).addPathPatterns("/**");
            }

        };
    }

}
