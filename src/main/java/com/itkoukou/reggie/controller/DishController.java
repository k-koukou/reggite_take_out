package com.itkoukou.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itkoukou.reggie.Dto.DishDto;
import com.itkoukou.reggie.common.R;
import com.itkoukou.reggie.pojo.Category;
import com.itkoukou.reggie.pojo.Dish;
import com.itkoukou.reggie.pojo.DishFlavor;
import com.itkoukou.reggie.pojo.Employee;
import com.itkoukou.reggie.service.CategoryService;
import com.itkoukou.reggie.service.DishFlavorService;
import com.itkoukou.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @className DishController
 * @userName 李院斌
 * @DATA 2023-02-10 18:31
 * @nickName 辻弎
 **/
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;
    //查询分页数据
    @GetMapping ("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    //添加菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        log.info ("发送的数据 :{}" ,dishDto );

        dishService.saveWithFlavor (dishDto);

        //精确的清理缓存   清理某一个菜品的分类
        String key = "dish_"+ dishDto.getCategoryId ()+"_1";
        redisTemplate.delete (key);

        return R.success ("新增菜品成功");
    }

    //更具id查询菜品信息以及口味信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable("id") Long id){
        log.info ("菜品以及口味的查询");
        DishDto byIdWithFlavor = dishService.getByIdWithFlavor (id);
        return R.success (byIdWithFlavor);
    }


    //修改之后保存菜品信息
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        log.info ("发送的数据 :{}" ,dishDto );

        dishService.UpdateWithFlavor (dishDto);

        //清理所有的菜品缓存数据
        //Set keys = redisTemplate.keys ("dish_*");
        //redisTemplate.delete (keys);

        //精确的清理缓存   清理某一个菜品的分类
        String key = "dish_"+ dishDto.getCategoryId ()+"_1";
        redisTemplate.delete (key);
        return R.success ("新增菜品成功");
    }


   /* //更具条件查询菜品
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){

        //构建条件对象
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<> ();
        lambdaQueryWrapper.eq (dish.getCategoryId () != null , Dish::getCategoryId , dish.getCategoryId ());

        //查询状态为1的  也就是起售的菜品
        lambdaQueryWrapper.eq (Dish::getStatus , 1);

        //添加一个排序条件
        lambdaQueryWrapper.orderByAsc (Dish::getSort).orderByDesc (Dish::getUpdateTime);

        List<Dish> list = dishService.list (lambdaQueryWrapper);

        return R.success (list);
    }
*/
   //更具条件查询菜品
   @GetMapping("/list")
   public R<List<DishDto>> list(Dish dish){

       List<DishDto> dishDtoList = null;

       String key = "dish_"+dish.getCategoryId ()+"_"+dish.getStatus ();
       //先从redis中获取缓存数据
       dishDtoList = (List<DishDto>) redisTemplate.opsForValue ().get (key);

       if (dishDtoList != null){
           //存在 直接给前端
           return R.success (dishDtoList);
       }
       //不存在 去数据库去查询
       //构建条件对象
       LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<> ();
       lambdaQueryWrapper.eq (dish.getCategoryId () != null , Dish::getCategoryId , dish.getCategoryId ());

       //查询状态为1的  也就是起售的菜品
       lambdaQueryWrapper.eq (Dish::getStatus , 1);

       //添加一个排序条件
       lambdaQueryWrapper.orderByAsc (Dish::getSort).orderByDesc (Dish::getUpdateTime);

       List<Dish> list = dishService.list (lambdaQueryWrapper);


       dishDtoList = list.stream().map((item) -> {
           DishDto dishDto = new DishDto();

           BeanUtils.copyProperties(item,dishDto);

           Long categoryId = item.getCategoryId();//分类id
           //根据id查询分类对象
           Category category = categoryService.getById(categoryId);

           if(category != null){
               String categoryName = category.getName();
               dishDto.setCategoryName(categoryName);
           }
           //菜品
           Long Dishid = item.getId ();

           LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper1 = new LambdaQueryWrapper<> ();
           lambdaQueryWrapper1.eq (DishFlavor::getDishId , Dishid);
           List<DishFlavor> list1 = dishFlavorService.list (lambdaQueryWrapper1);
           dishDto.setFlavors (list1);
           return dishDto;
       }).collect(Collectors.toList());

       //缓存到redis
       redisTemplate.opsForValue ().set (key , dishDtoList,60, TimeUnit.MINUTES);

       return R.success (dishDtoList);
   }
}
