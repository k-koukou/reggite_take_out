package com.itkoukou.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itkoukou.reggie.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}