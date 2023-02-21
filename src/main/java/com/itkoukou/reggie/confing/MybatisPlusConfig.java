package com.itkoukou.reggie.confing;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @className MybatisPlusConfig
 * @userName 李院斌
 * @DATA 2023-02-08 12:37
 * @nickName 辻弎
 **/
/*
* mybatis-plus 的分页插件 配置分页的插件
*
* 这是一个配置类
* */
    @Configuration  //注解说明这是一个配置类
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
