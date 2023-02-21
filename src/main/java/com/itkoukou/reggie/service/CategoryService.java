package com.itkoukou.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itkoukou.reggie.pojo.Category;

/*
*
* 菜品的分类
* */
public interface CategoryService extends IService<Category> {

    //根据id删除菜系
    void remove(Long ids);
}
