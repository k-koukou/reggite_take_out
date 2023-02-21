package com.itkoukou.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itkoukou.reggie.Dto.SetmealDto;
import com.itkoukou.reggie.pojo.Setmeal;

import java.util.List;

/**
 * @className SetmealService
 * @userName 李院斌
 * @DATA 2023-02-10 14:02
 * @nickName 辻弎
 *
 *          菜品
 **/

public interface SetmealService extends IService<Setmeal> {

    //新增菜品同时保存套餐以及菜品的关联关系
    void saveWithDish(SetmealDto setmealDto);

    //删除套餐以及关联的产品
    void removeWithDish(List<Long> ids);
}
