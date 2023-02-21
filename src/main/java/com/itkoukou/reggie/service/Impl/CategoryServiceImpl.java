package com.itkoukou.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itkoukou.reggie.common.CustomException;
import com.itkoukou.reggie.mapper.CategoryMapper;
import com.itkoukou.reggie.pojo.Category;
import com.itkoukou.reggie.pojo.Dish;
import com.itkoukou.reggie.pojo.Setmeal;
import com.itkoukou.reggie.service.CategoryService;
import com.itkoukou.reggie.service.DishService;
import com.itkoukou.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    //根据id删除分类
    @Override
    public void remove (Long ids) {
        //查询当前分类是不是关联了菜品    yes  throw exception
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<> ();
            //添加查询条件
        queryWrapper.eq (Dish::getCategoryId , ids);
            //查询
         int count = dishService.count (queryWrapper);
         //判断
        if (count > 0){
            //关联了菜品
            throw new CustomException ("当前分类关联了菜品，不能删除");
        }

        //查询当前菜品是不是关联了分类    yes  throw exception
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<> ();
        lambdaQueryWrapper.eq (Setmeal::getCategoryId , ids);
         int count1 = setmealService.count (lambdaQueryWrapper);
         if (count1 > 0 ){
             //关联了套餐
             throw new CustomException ("当前分类关联了套餐，不能删除");
         }
        //都没有  remove       正常删除

        super.removeById(ids);
    }
}
