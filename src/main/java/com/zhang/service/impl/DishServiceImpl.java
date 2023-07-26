package com.zhang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.dto.DishDto;
import com.zhang.mapper.DishMapper;
import com.zhang.pojo.Dish;
import com.zhang.pojo.DishFlavor;
import com.zhang.service.DishFlavorService;
import com.zhang.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2023-07-20 17:50
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     *  用来分别保存dish和dishFlavor的数据
     * @param dishDto
     */
    @Override
    public void saveWithDishFlavor(DishDto dishDto) {
        //???? why
        this.save(dishDto);

        Long dishId = dishDto.getId();
        //DishFlavor中含有category属性，但是从页面获取的数据中没有这个属性对应的值，所以得在获取到的DishFlavorList提供这个值
       //一个jdk8新特性的写法
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //批量保存
        dishFlavorService.saveBatch(flavors);
    }

//    根据dishId来查找对应的口味数据和菜品信息
    @Override
    public DishDto getWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(lqw);
        dishDto.setFlavors(flavors);
        return dishDto;
    }


//    更新菜品操作，要更新菜品信息以及对应的口味信息
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //这里直接调用更新操作，传入的值是dish的子类dishDto，会直接更新其中的dish的值
         this.updateById(dishDto);
         //对菜品口味的处理
        //1.将口味信息清除
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper();
        lqw.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lqw);
        //2.新增dishDto中的flavor数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        //这里的flavor中没有dishId数据，所以要对其进行处理在更新
        flavors = flavors.stream().map((item) ->{
        Long id = dishDto.getId();
        item.setDishId(id);
        return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
