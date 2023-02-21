package com.itkoukou.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itkoukou.reggie.common.R;
import com.itkoukou.reggie.pojo.Category;
import com.itkoukou.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private com.itkoukou.reggie.service.CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);
        //分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    //根据ID删除分类
    @DeleteMapping()
    public R<String> delete(Long ids){
        log.info ("删除id为" +ids +"的菜品");

//        categoryService.removeById (ids);
        categoryService.remove (ids);

        return R.success ("分类信息删除成功");
    }

    //更具ID修改分类信息
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info ("修改分类信息 : {}" , category);

        categoryService.updateById (category);

        return R.success ("修改分类信息成功");
    }

    //更具条件查询
    @GetMapping("/list")
    public R<List<Category>> listR(Category category){
        //条件构造
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<> ();
        //添加条件
        lambdaQueryWrapper.eq (category.getType () != null , Category :: getType , category.getType ());
        //添加排序
        lambdaQueryWrapper.orderByAsc (Category::getSort).orderByAsc (Category::getUpdateTime);

        List<Category> list = categoryService.list (lambdaQueryWrapper);
        return R.success (list);
    }
}
