package com.itkoukou.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itkoukou.reggie.Dto.SetmealDto;
import com.itkoukou.reggie.common.R;
import com.itkoukou.reggie.pojo.Category;
import com.itkoukou.reggie.pojo.Setmeal;
import com.itkoukou.reggie.service.CategoryService;
import com.itkoukou.reggie.service.SetmealDishService;
import com.itkoukou.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @className SetmealController
 * @userName 李院斌
 * @DATA 2023-02-12 17:49
 * @nickName 辻弎
 *          套餐管理
 **/
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    public SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;
    //保存套餐信息    新增套餐
    @PostMapping
    public R<String> save (@RequestBody SetmealDto setmealDto){

        log.info ("新增套餐:{}" , setmealDto);

        setmealService.saveWithDish (setmealDto);

        return R.success ("新增菜品成功");
    }

    //分页查询
    //请求 URL: http://localhost:8080/setmeal/page?page=1&pageSize=10
    //GET
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize , String name){
        //构造分页构造器
        Page<Setmeal> pageInfo = new Page<> (page ,pageSize);
        Page<SetmealDto> pageInfo1 = new Page<> ();
        //创造数据
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<> ();
        //添加查询条件
        lambdaQueryWrapper.like (name != null , Setmeal::getName , name);
        lambdaQueryWrapper.orderByDesc (Setmeal::getUpdateTime);
        //添加排序条件
        setmealService.page (pageInfo , lambdaQueryWrapper);
        //拷贝数据到1中
        BeanUtils.copyProperties (pageInfo , pageInfo1,"records");
        List<Setmeal> records = pageInfo.getRecords ();
        List<SetmealDto> list = records.stream ().map ((itme)->{
            SetmealDto setmealDto = new SetmealDto ();
            BeanUtils.copyProperties (itme , setmealDto);
            //分类ID
            Long categoryId = itme.getCategoryId ();
            //根据ID查询套餐
            Category byId = categoryService.getById (categoryId);
            if (byId != null){
                String byIdName = byId.getName ();
                setmealDto.setCategoryName (byIdName);
            }
            return setmealDto;
        }).collect (Collectors.toList ());

        pageInfo1.setRecords (list);

        return R.success (pageInfo1);
    }

    /*
    * 请求 URL: http://localhost:8080/setmeal?ids=1624721338121777154
      请求方法: DELETE
      * 删除套餐的功能
    */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info ("ids的值 : {}",ids);
        setmealService.removeWithDish(ids);
        return R.success ("套餐数据删除成功");
    }
}
