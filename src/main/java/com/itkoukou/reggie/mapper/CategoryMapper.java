package com.itkoukou.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itkoukou.reggie.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 辻弎
 * @Author: 李院斌
 * @Date: 2023/2/10 13:59
 * @returns: a
 * @Description:        套餐的mapper
 * **/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
