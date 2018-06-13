package com.sunlands.library.controller;

import com.sunlands.library.domain.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : hulin
 * @date : 2018/6/7 11:24
 * @description :
 */
@RestController
public class TestController {

    public String test;
    public String type;

    @Autowired
    private TestConfig testConfig;


    @RequestMapping("/test")
    public String test(){
        return testConfig.getTest()+"123";
    }
}
