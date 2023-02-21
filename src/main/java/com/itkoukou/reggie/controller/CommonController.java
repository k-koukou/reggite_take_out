package com.itkoukou.reggie.controller;

/**
 * @className CommonController
 * @userName 李院斌
 * @DATA 2023-02-10 14:56
 * @nickName 辻弎
 **/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itkoukou.reggie.common.R;
import com.itkoukou.reggie.pojo.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: 辻弎
 * @Author: 李院斌
 * @Date: 2023/2/10 14:56
 * @returns:
 * @Description:    主要去实现文件的上传以及下载的功能
 **/
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    //声明一个变量
    @Value ("${reggie.path}")
    private String basePath;

    //文件上传
    @PostMapping("/upload")
    public R<String> upload (MultipartFile file){   //这个参数的名称要和前端的响应相同

        String originalFilename = file.getOriginalFilename ();//获取原始的文件名称
        assert originalFilename != null;
        String substring = originalFilename.substring (originalFilename.lastIndexOf (".")); //文件后缀
        //UUID 生成文件名
        String fileName = UUID.randomUUID ().toString ()+substring;
         //创建一个目录结构
        File file1 = new File (basePath);
        //判断当前目录是否存在
        if(!file1.exists ()){
            //目录不存在，使用方法进行目录的创建
            file1.mkdirs ();
        } //目录存在执行一下的代码
        try {
            file.transferTo (new File (basePath+ fileName));
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return R.success (fileName);
    }

    //文件的下载
    @GetMapping("/download")
    public void  download(String name, HttpServletResponse response){
        try {
            //输入流  读取文件内容
             FileInputStream fileInputStream = new FileInputStream (new File (basePath+name));

            //输出流  将文件写回浏览器
             ServletOutputStream outputStream = response.getOutputStream ();

             //设置格式
             response.setContentType ("image/jpeg");


             int len = 0;
             byte[] bate = new byte[1024];
             while ((len = fileInputStream.read (bate)) != -1){
                 outputStream.write (bate , 0 , len);
                 outputStream.flush ();
             }

             outputStream.close ();
             fileInputStream.close ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

}
