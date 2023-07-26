package com.zhang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.mapper.ShoppingCartMapper;
import com.zhang.pojo.ShoppingCart;
import com.zhang.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author shkstart
 * @create 2023-07-24 16:25
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
