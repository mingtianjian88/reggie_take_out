package com.zhang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.dto.DishDto;
import com.zhang.pojo.Dish;

/**
 * @author shkstart
 * @create 2023-07-20 17:50
 */
public interface DishService extends IService<Dish> {

    //新增一个方法，分别存Dish 和 DishFlavor的数据到数据库中
    public void saveWithDishFlavor(DishDto dishDto);

    //根据dishId来查找对应的口味数据和菜品信息
    public DishDto getWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
