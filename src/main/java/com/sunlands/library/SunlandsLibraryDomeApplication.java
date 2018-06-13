package com.sunlands.library;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan("com.sunlands.library.mapper")
@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
@EnableAutoConfiguration
@EnableCaching
public class SunlandsLibraryDomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SunlandsLibraryDomeApplication.class, args);
    }
}
