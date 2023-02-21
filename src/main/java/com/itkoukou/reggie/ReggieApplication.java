package com.itkoukou.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @className ReggieApplication
 * @userName 李院斌
 * @DATA 2023-02-08 00:17
 * @nickName 辻弎
 **/
//spring boot的启动类
@SpringBootApplication
//日志注解
@Slf4j
//扫描组件 也就是wenfilter拦截器的请求
@ServletComponentScan
//开启事务的注解管理
@EnableTransactionManagement
public class ReggieApplication {
    public static void main (String[] args) {
        SpringApplication.run (ReggieApplication.class , args);
        log.info ("主程序MAIN方法开始启动");
    }
}
