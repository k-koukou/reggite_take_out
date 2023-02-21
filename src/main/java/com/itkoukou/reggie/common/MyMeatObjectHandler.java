package com.itkoukou.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @className MyMeatObjectHandler
 * @userName 李院斌
 * @DATA 2023-02-08 23:06
 * @nickName 辻弎
 **/

/**
 * @Author: 辻弎
 * @Author: 李院斌
 * @Date: 2023/2/8 23:06
 * @returns:
 * @Description: 元数据对象处理器
 **/

@Component

@Slf4j
public class MyMeatObjectHandler implements MetaObjectHandler {

    /*插入操作自动填充*/
    @Override
    public void insertFill (MetaObject metaObject) {
        log.info ("公共字段的自动填充--{insert}");
        //设置时间
        metaObject.setValue ("createTime" , LocalDateTime.now ());
        metaObject.setValue ("updateTime" , LocalDateTime.now ());
        metaObject.setValue ("createUser", BaseContext.getCurrentById ());
        metaObject.setValue ("updateUser",BaseContext.getCurrentById ());
    }
    /*更新时候自动填充*/
    @Override
    public void updateFill (MetaObject metaObject) {
        log.info ("公共字段的自动填充--{update}");
        metaObject.setValue ("updateTime" , LocalDateTime.now ());
        metaObject.setValue ("updateUser",BaseContext.getCurrentById ());
    }
}
