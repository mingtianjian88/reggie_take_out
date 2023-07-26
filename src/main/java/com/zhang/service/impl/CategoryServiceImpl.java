package com.zhang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.common.MyException;
import com.zhang.mapper.CategoryMapper;
import com.zhang.pojo.Category;
import com.zhang.pojo.Dish;
import com.zhang.pojo.Setmeal;
import com.zhang.service.CategoryService;
import com.zhang.service.DishService;
import com.zhang.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shkstart
 * @create 2023-07-20 15:47
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 用来处理删除操作，当该数据含有外联数据时，抛出异常，不能删除
     * @param id
     */
    public void remove(Long id){

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        //根据传入的categoryId 来查找其关联的菜品的个数
        int countDish = dishService.count(dishLambdaQueryWrapper);
        if (countDish > 0){
            //如果关联了菜品，抛出自定义的业务异常
            throw new MyException("当前菜品分类关联了多个菜品，不能删除，请先删除关联的菜品");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int countSetmeal = setmealService.count(setmealLambdaQueryWrapper);
        if (countSetmeal > 0 ){
            //如果套餐分类关联了对应的套餐，抛出自定义的业务异常
            throw new MyException("当前套餐分类关联了多个套餐，不能删除，请先删除关联的套餐");
        }

        super.removeById(id);
    }
}
