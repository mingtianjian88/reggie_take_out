package com.zhang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.mapper.OrderDetailMapper;
import com.zhang.pojo.OrderDetail;
import com.zhang.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author shkstart
 * @create 2023-07-24 21:06
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
