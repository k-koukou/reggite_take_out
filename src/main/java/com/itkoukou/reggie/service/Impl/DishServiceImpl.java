package com.itkoukou.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itkoukou.reggie.Dto.DishDto;
import com.itkoukou.reggie.common.R;
import com.itkoukou.reggie.mapper.DishMapper;
import com.itkoukou.reggie.pojo.Dish;
import com.itkoukou.reggie.pojo.DishFlavor;
import com.itkoukou.reggie.service.DishFlavorService;
import com.itkoukou.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @className DishServiceImpl
 * @userName 李院斌
 * @DATA 2023-02-10 14:05
 * @nickName 辻弎
 **/
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper , Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    //新增菜品同时新增菜品的口味     也就是同时操作两张表
    @Override
    @Transactional
    public void saveWithFlavor (DishDto dishDto) {
        //保存菜品的基本信息到达dish表
        this.save (dishDto);

        Long id = dishDto.getId ();   //菜品ID

        //数据处理
         List<DishFlavor> flavors = dishDto.getFlavors ();
         flavors = flavors.stream ().map ((item)->
         {
             item.setDishId (id);
             return item;
         }).collect (Collectors.toList ());
        //保存菜品口味到dishflavor
        dishFlavorService.saveBatch (flavors);
    }

    // //更具ID查询菜品信息以及口味信息
    @Override
    public DishDto getByIdWithFlavor (Long id) {
        //查询菜品的基本信息
        Dish byId = this.getById (id);

        //对象拷贝
        DishDto dishDto = new DishDto ();
        BeanUtils.copyProperties (byId,dishDto);
        //查询菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<> ();
        lambdaQueryWrapper.eq (DishFlavor::getDishId , byId.getId ());
        List<DishFlavor> list = dishFlavorService.list (lambdaQueryWrapper);

        dishDto.setFlavors (list);

        return dishDto;
    }

    //更新菜品信息 更新口味信息
    @Override
    @Transactional
    public void UpdateWithFlavor (DishDto dishDto) {
        //跟新DISH表信息
        this.updateById (dishDto);
        //清理当前菜品的口味信息
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<> ();
        lambdaQueryWrapper.eq (DishFlavor::getDishId , dishDto.getId ());
        dishFlavorService.remove (lambdaQueryWrapper);
        //跟新口味表
        List<DishFlavor> flavors = dishDto.getFlavors ();
        flavors.stream ().map ((imte)->{
            imte.setDishId (dishDto.getId ());
            return imte;
        }).collect (Collectors.toList ());
        dishFlavorService.saveBatch (flavors);
    }



}
