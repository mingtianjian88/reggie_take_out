package com.zhang.controller;


import com.zhang.common.R;
import com.zhang.pojo.Orders;
import com.zhang.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author shkstart
 * @create 2023-07-24 20:48
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

   @PostMapping("/submit")
    public R<String> save(@RequestBody Orders orders){
        ordersService.save(orders);
        return R.success("保存成功!");
   }
}

