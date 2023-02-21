package com.itkoukou.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itkoukou.reggie.common.R;
import com.itkoukou.reggie.pojo.Employee;
import com.itkoukou.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @className EmployeeController
 * @userName 李院斌
 * @DATA 2023-02-08 00:57
 * @nickName 辻弎
 **/
@Slf4j  //日志调试
@RestController //返回给浏览器实体资源
@RequestMapping("/employee")
public class EmployeeController {

    //自动装配service层 控制层
    @Autowired
    private EmployeeService service;

    //登录
    @PostMapping("/login")     //  @RequestBody   接收json的数据格式的时候使用
    public R<Employee> login(@RequestBody Employee employee , HttpServletRequest request){
        //获取密码并且封装
         String password = employee.getPassword ();
        //将页面提交的密码进行MD5的形式加密处理
         password = DigestUtils.md5DigestAsHex (password.getBytes(StandardCharsets.UTF_8));
        //更具用户名称查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<> ();
        queryWrapper.eq (Employee::getUsername , employee.getUsername ());
        //调用业务层处理数据
         Employee emp = service.getOne (queryWrapper);
         //判断账户 如果没有查询到就返回登录失败的结果
        if (emp == null){
            return R.error ("登录失败-用户名称错误");
        }
        //用户名称校验正确，下一步进行密码的比对
        if (!emp.getPassword ().equals (password)){
            return R.error ("登录失败-密码错误");
        }
        //密码比对成功  查看状态是不是禁用的状态 0==禁用
        if (emp.getStatus ()  ==  0){
            return R.error ("该用户涉嫌违规已被管理员禁用");
        }
        //账户正常   最后一步 登录成功将员工的ID存入session域中并且返回登录成功的结果
         HttpSession session = request.getSession ();
        session.setAttribute ("employee" , emp.getId ());
        return R.success (emp);
    }

    //员工退出登录的功能
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理session中存储的员工的信息
        request.getSession ().removeAttribute ("employee");
        return R.success ("退出成功-欢迎下次使用");
    }

    //添加员工信息 addUser
    @PostMapping
    public R<String> save(@RequestBody Employee employee , HttpServletRequest request){
        log.info ("新增员工 ， 员工的信息 --> :{}" , employee.toString ());
        //给定一个初始化的密码 需要使用md5加密
        employee.setPassword (DigestUtils.md5DigestAsHex ("123456".getBytes(StandardCharsets.UTF_8)));
/**
        //创建时间  就是当前管理者注册用户的时候使用的时间    LocalDateTime.now ()  获取当前的时间
        employee.setCreateTime (LocalDateTime.now ());
        //更新时间
        employee.setUpdateTime (LocalDateTime.now ());
        //获得当前用户登录的ID

        Long Long = (Long) request.getSession ().getAttribute ("employee");
        //注册人的ID
        employee.setCreateUser (Long);
        //更新人的ID
        employee.setUpdateUser (Long);
 */
        //调用service
        service.save (employee);

        return R.success ("员工信息录入成功");
    }

    //员工信息分页查询的方法   page?page=1&pageSize=10&name=%E6%9D%8E%E5%85%8B
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize , String name){
        log.info ("page = {} , pageSize = {} , name = {}" ,page , pageSize , name);
        //构造分页插件的构造器
        Page pageInfo = new Page (page ,pageSize);
        //条件构造器
        LambdaQueryWrapper <Employee> queryWrapper = new LambdaQueryWrapper<> ();
        //添加条件过滤
        queryWrapper.like (StringUtils.isNotEmpty (name) , Employee ::getName , name);
        //添加一个条件排序
        queryWrapper.orderByDesc (Employee::getUpdateTime);
        //执行查询
        service.page (pageInfo , queryWrapper);
        //返回结果
        return R.success (pageInfo);
    }

    //禁用或者是编辑员工的信息  实际就是更新员工的信息
    @PutMapping
    public R<String> update(@RequestBody Employee employee , HttpServletRequest request) {

        log.info ("employee 中的数据 :  {}", employee.toString ());
/**
        Long empId = (Long) request.getSession ().getAttribute ("employee");

        //修改时间
//        employee.setUpdateTime (LocalDateTime.now ());
        //修改该数据是谁更新的
        employee.setUpdateUser (empId);

 */
        service.updateById (employee);

        return R.success ("员工信息修改成功");
    }

    //修改数据的页面回显 ，先更具ID查询员工信息
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id){
        log.info ("更具id查询员工信息:{}" , id);
        //调用业务层处理数据
         Employee byId = service.getById (id);
         if(byId != null) {
             //返回给统一的类进行json数据回调
             return R.success (byId);
         }
         return R.error ("没有该员工的信息");
    }
}
