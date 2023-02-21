package com.itkoukou.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itkoukou.reggie.Dto.SetmealDto;
import com.itkoukou.reggie.common.CustomException;
import com.itkoukou.reggie.mapper.SetmealMapper;
import com.itkoukou.reggie.pojo.Setmeal;
import com.itkoukou.reggie.pojo.SetmealDish;
import com.itkoukou.reggie.service.SetmealDishService;
import com.itkoukou.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @className SetmealServiceImpl
 * @userName 李院斌
 * @DATA 2023-02-10 14:06
 * @nickName 辻弎
 **/
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper , Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    //新增菜品同时保存套餐以及菜品的关联关系
    @Transactional //事务注解
    @Override
    public void saveWithDish (SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save (setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes ();

        setmealDishes.stream().map ((item)->{
            item.setSetmealId (setmealDto.getId ());
            return item;
        }).collect (Collectors.toList ());

        //保存套餐以及菜品的关联信息   insert
        setmealDishService.saveBatch (setmealDishes);
    }

    //删除套餐以及关联的产品
    @Override
    public void removeWithDish (List<Long> ids) {
        //查询套餐的确定是否是可用的数据 是不是可以进行删除
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<> ();
            //如果不能删除抛出异常
        lambdaQueryWrapper.in (Setmeal::getId , ids );
        lambdaQueryWrapper.eq (Setmeal::getStatus , 1);
        int count = this.count (lambdaQueryWrapper);
        if (count > 0 ){
            throw new CustomException ("套餐正在售卖中无法删除");
        }
        //如果可以删除 首先删除套餐表中的数据
        this.removeByIds (ids);
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper1 = new LambdaQueryWrapper<> ();

        lambdaQueryWrapper1.in (SetmealDish::getSetmealId ,ids);

        setmealDishService.remove (lambdaQueryWrapper1);
    }
}
