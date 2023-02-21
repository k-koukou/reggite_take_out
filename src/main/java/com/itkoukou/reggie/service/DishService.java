package com.itkoukou.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itkoukou.reggie.Dto.DishDto;
import com.itkoukou.reggie.pojo.Dish;

/**
 * @className DishService
 * @userName 李院斌
 * @DATA 2023-02-10 14:04
 * @nickName 辻弎
 *  套餐
 **/
public interface DishService extends IService<Dish> {

    //新增菜品同时新增菜品的口味     也就是同时操作两张表
    void saveWithFlavor(DishDto dishDto);

    //更具ID查询菜品信息以及口味信息
    DishDto getByIdWithFlavor(Long id);

    //更新菜品信息 更新口味信息
    void UpdateWithFlavor (DishDto dishDto);
}
