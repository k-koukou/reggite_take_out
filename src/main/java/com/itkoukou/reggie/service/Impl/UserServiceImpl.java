package com.itkoukou.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itkoukou.reggie.mapper.UserMapper;
import com.itkoukou.reggie.pojo.User;
import com.itkoukou.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @className UserServiceImpl
 * @userName 李院斌
 * @DATA 2023-02-12 20:34
 * @nickName 辻弎
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper , User> implements UserService {
}
