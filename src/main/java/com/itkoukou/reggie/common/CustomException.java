package com.itkoukou.reggie.common;

/**
 * @className CustomExcepution
 * @userName 李院斌
 * @DATA 2023-02-10 14:21
 * @nickName 辻弎
 *          自定义的业务异常
 **/
public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }
}
