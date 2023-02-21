package com.itkoukou.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itkoukou.reggie.mapper.OrderDetailMapper;
import com.itkoukou.reggie.pojo.OrderDetail;
import com.itkoukou.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}