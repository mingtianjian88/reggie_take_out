package com.zhang.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhang.common.BaseContext;
import com.zhang.common.R;
import com.zhang.pojo.ShoppingCart;
import com.zhang.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shkstart
 * @create 2023-07-24 16:26
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

 @Autowired
 private ShoppingCartService shoppingCartService;

    /**
     * 将菜品或套餐添加到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //页面中发送过来的数据是没有userId的，要手动添加
        //从ThreadLocal中获取
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //根据setmealId或者dishId来查
        //先判断
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        lqw.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        //根据条件查询出来的对象，判断是否存在，
        ShoppingCart cartServiceOne = shoppingCartService.getOne(lqw);
        if (cartServiceOne != null){
            // 如果存在number+1，
            cartServiceOne.setNumber(cartServiceOne.getNumber()+1);
            shoppingCartService.updateById(cartServiceOne);
        }else {
            // 不存在默认number为一
           shoppingCart.setNumber(1);
           shoppingCartService.save(shoppingCart);
           cartServiceOne = shoppingCart;
        }

       return R.success(cartServiceOne);
    }


    /**
     * 展示购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.in(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        return R.success(list);

    }


    @DeleteMapping("clean")
    public R<String> delete(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.in(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        if (!(list.size()>0)){
            //如果查出来的购物车中没有信息
            return R.error("请添加商品!");
        }
        shoppingCartService.remove(lqw);
        return R.success("删除成功!");
    }

}
