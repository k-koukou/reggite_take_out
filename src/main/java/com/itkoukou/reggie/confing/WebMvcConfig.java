package com.itkoukou.reggie.confing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @className WebMvcConfig
 * @userName 李院斌
 * @DATA 2023-02-08 00:27
 * @nickName 辻弎
 **/
//  说明这是一个配置类
@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /*
    * 设置静态资源映射
    * */
    @Override
    protected void addResourceHandlers (ResourceHandlerRegistry registry) {
        log.info ("修改默认访问静态资源的位置-----开始资源的映射");
        registry.addResourceHandler ("/backend/**").addResourceLocations ("classpath:/backend/");
        registry.addResourceHandler ("/front/**").addResourceLocations ("classpath:/front/");
    }

    /*
    * 扩展mvc框架的转化器
    * */
    @Override
    protected void extendMessageConverters (List<HttpMessageConverter<?>> converters) {
        log.info ("扩展消息转换器————————————————————");
        //创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter ();
        //设置对象转换器 底层使用的是jackson 将java类型转换为json类型
        messageConverter.setObjectMapper (new com.itkoukou.reggie.common.JacksonObjectMapper ());
        //将这个转换器转化为mvc框架的转换器集合中
        converters.add (0 , messageConverter);
    }
}
