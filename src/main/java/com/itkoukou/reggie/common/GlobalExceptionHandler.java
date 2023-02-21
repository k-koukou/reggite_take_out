package com.itkoukou.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @className GlobalExceptionHandler
 * @userName 李院斌
 * @DATA 2023-02-08 12:03
 * @nickName 辻弎
 **/
/*
全局异常处理
* */
    @ControllerAdvice (annotations = {RestController.class , Controller.class})   //拦截controller的异常

    @RestController

    @Slf4j
public class GlobalExceptionHandler {
        /*
        异常方法的处理信息
        * */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){

        log.info (exception.getMessage ()); //日志输出异常处理信息

        //判断当前错误信息
        if (exception.getMessage ().contains ("Duplicate entry")){
            // Duplicate entry 'like' for key 'employee.idx_username'
             String[] s = exception.getMessage ().split (" ");  //根据空格查询错误信息以便于进一步提示

             String msg = "用户" +s[2] + "已经存在";

             return R.error (msg);
        }
        return R.error ("未知错误");
    }


    /*捕获分类异常*/
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception){

        log.info (exception.getMessage ()); //日志输出异常处理信息

        return R.error (exception.getMessage ());
    }
}
