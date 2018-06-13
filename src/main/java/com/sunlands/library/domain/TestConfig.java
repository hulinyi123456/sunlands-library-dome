package com.sunlands.library.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : hulin
 * @date : 2018/6/7 18:55
 * @description :
 */
@Component
@ConfigurationProperties("spring")
public class TestConfig {
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
