package com.itkoukou.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itkoukou.reggie.common.BaseContext;
import com.itkoukou.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @className LoginCheckFilter
 * @userName 李院斌
 * @DATA 2023-02-08 10:35
 * @nickName 辻弎
 **/
/*过滤器  拦截请求的url
* 判断用户是不是已经完成登录
*
* 在启动类添加@ServletComponentScan注解后，会自动将带有@WebFilter的注解进行注入！
* */
    @WebFilter(filterName="LoginCheckFilter" ,   urlPatterns= "/*")   // filterName 拦截器的名称   urlPatterns 拦截的路径
    @Slf4j
public class LoginCheckFilter implements Filter {
        //定义静态的私有的路劲匹配的方法    路径匹配器 ，匹配通配符
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher ();
        //过滤的方法
    @Override
    public void doFilter (ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //首先强制转换类型
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获得本次请求的url
         String requestURI = request.getRequestURI ();
         log.info ("拦截到请求: {}",requestURI);
         //判断本次的请求是不是需要被拦截，指定某些路径
        String[] urls = new String[]{
                //PC端
                "/employee/login" , //登录请求
                "/employee/logout", //退出请求
                "/backend/**",     //静态资源
                //移动端
                "/front/**",
                "/user/sendMsg",
                "/user/login",
                //其他
                "/common/**"
        };
        //判断本次的请求是不是需要被处理
         boolean check = check (urls, requestURI);
         //如果不需要处理就直接放行
        if (check){
            log.info ("拦截到请求: {} 该请求不需要被处理",requestURI);
            filterChain.doFilter (request , response);
            return;
        }
        //判断用户登录的状态 如果已经登录 就直接放行        PC端
        if(
        request.getSession ().getAttribute ("employee")!= null
        ){
            log.info ("用户已经登录了 用户ID为 : {}",request.getSession ().getAttribute ("employee"));
            //拿到session中的数据
            Long employee = (Long) request.getSession ().getAttribute ("employee");
            //通过封装的类存储
            BaseContext.setCurrentId (employee);
            filterChain.doFilter (request , response);
            return;
        }

        //判断用户登录的状态 如果已经登录 就直接放行        移动端
        if(
                request.getSession ().getAttribute ("user")!= null
        ){
            log.info ("用户已经登录了 用户ID为 : {}",request.getSession ().getAttribute ("user"));
            //拿到session中的数据
            Long user = (Long) request.getSession ().getAttribute ("user");
            //通过封装的类存储
            BaseContext.setCurrentId (user);
            filterChain.doFilter (request , response);
            return;
        }

        //如果没有登录就返回没有没有登录的结果   通过输出流向前台返回数据
        log.info ("用户没有去登录");
        response.getWriter ().write (JSON.toJSONString (R.error ("NOTLOGIN")));
        return;
    }
    /**
     * @Author: 辻弎
     * @Author: 李院斌
     * @Date: 2023/2/8 11:03
     * @returns: boolean
     * @Description:  检查这次的路径是否被拦截或者说是是否在放行的路径中
     **/
    public boolean check(String[] urls , String requestURL){
        for (String url : urls) {
             boolean match = PATH_MATCHER.match (url, requestURL);
             if (match){
                 return true;
             }
        }
        return false;
    }
}
