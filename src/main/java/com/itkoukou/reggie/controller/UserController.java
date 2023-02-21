package com.itkoukou.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itkoukou.reggie.Utils.ValidateCodeUtils;
import com.itkoukou.reggie.common.R;
import com.itkoukou.reggie.pojo.User;
import com.itkoukou.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @className UserController
 * @userName 李院斌
 * @DATA 2023-02-12 20:35
 * @nickName 辻弎
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserService userService;

    //服务端注册
    @PostMapping ("/sendMsg")
    public R<String> sendMsg(@RequestBody User user , HttpSession model){
        //获取手机号码
        String phone = user.getPhone ();

        if (StringUtils.isNotEmpty (phone)) {

            //生成验证码
            String s = ValidateCodeUtils.generateValidateCode (6).toString ();

            log.info ("code:{}",s);
            //调用阿里云发送短信
    /**
            SMSUtils.sendMessage ("瑞吉外卖" ,"" , phone , s );

    */        //存储到session 以便于登录使用

            model.setAttribute (phone , s);

            return R.success ("手机验证码短信发送成功");
        }
        return R.error ("手机验证码短信发送失败");
    }

    //服务端登录
    @PostMapping ("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);

        if (codeInSession != null && codeInSession.equals (code)){
            //如果成立就是登录成功

            //判断是不是新的用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<> ();
            queryWrapper.eq (User::getPhone , phone);

            User user = userService.getOne (queryWrapper);
            if (user == null){
                //新用户
                user = new User ();
                user.setPhone (phone);
                user.setStatus (1);
                userService.save (user);
            }
            //登录成功
            session.setAttribute ("user" , user.getId ());
            return R.success (user);
        }
        //失败就登录失败
        return R.error ("登录失败-验证码错误");
    }


}
