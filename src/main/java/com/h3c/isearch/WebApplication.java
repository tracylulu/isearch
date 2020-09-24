package com.h3c.isearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@MapperScan("com.h3c.isearch.mapper")
@SpringBootApplication
@EnableAutoConfiguration
public class WebApplication /*implements CommandLineRunner*/ {


    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }


}


