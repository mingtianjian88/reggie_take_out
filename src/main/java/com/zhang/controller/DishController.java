package com.zhang.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.common.R;
import com.zhang.dto.DishDto;
import com.zhang.pojo.Category;
import com.zhang.pojo.Dish;
import com.zhang.pojo.DishFlavor;
import com.zhang.service.CategoryService;
import com.zhang.service.DishDtoService;
import com.zhang.service.DishFlavorService;
import com.zhang.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2023-07-20 23:00
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishDtoService dishDtoService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页处理
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){

        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(name != null,Dish::getName,name);
        dishService.page(dishPage, queryWrapper);
        //BeanUtils.copyProperties复制属性数值到指定的类中，
        // 这里dishPage作为源数据，dishDtoPage作为指定的类，ignoreProperties指定不需要复制的属性值
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        //因为Dish实体类没有菜品分类名称的属性，而DishDto实体类中含有这个CategoryName属性，
        // 所以我们需要把数据封装到DishDto作为泛型的Page中
        List<Dish> dishs = dishPage.getRecords();
        List<DishDto> dishDtoList = dishs.stream().map((item) ->{
            DishDto dishDto = new DishDto();
            //将item中的数据copy到dishDto中的，因为这里的DishDto是new出来的，所以对应的Dish是没有数据的，数据在item中
            BeanUtils.copyProperties(item,dishDto);
            //这里我们需要拿到categoryId，并根据categoryId查到对应的菜品分类，然后赋值给CategoryName属性
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null){
                //只是需要菜品分类名称
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        //这里的数据既有Dish 又有 DishFlavor的数据，但Dish'中没有DishFlavor属性，所以这里用到了DishDto类来接收数据
        dishService.saveWithDishFlavor(dishDto);
      return R.success("保存成功！");
    }

    /**
     * 根据dishId来查找对应的口味数据和菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        //这里回显数据需要口味数据和菜品信息的数据，重写方法
        DishDto dishDto = dishService.getWithFlavor(id);
        return R.success(dishDto);
    }


    /**
     * 更新菜品操作，要更新菜品信息以及对应的口味信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功！");
    }


    /**
     * 删除及批量删除功能(不知道自己写的对不对，功能可以实现)
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam String ids){
        //要删除对应的dish，还要删除对应的dishFlavor
        //这里传入的ids是一个字符串，会存在多个值
        ArrayList<Long> list = new ArrayList<>();
        String[] strs = ids.split(",");
        for (String str : strs) {
            Long value = Long.valueOf(str);
            list.add(value);
        }
        dishService.removeByIds(list);
        for (Long dishId : list) {
            LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
            lqw.eq(DishFlavor::getDishId,dishId);
            dishFlavorService.remove(lqw);
        }
        return R.success("删除成功！");
    }

//    /**
//     * 根据菜品分类Id来查询菜品信息
//     * @param categoryId
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List> list(@RequestParam Long categoryId){
//        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
//        lqw.eq(Dish::getCategoryId,categoryId);
//        List<Dish> list = dishService.list(lqw);
//        return R.success(list);
//    }
    /**
     * 根据菜品分类Id来查询菜品信息以及口味信息
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        lqw.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(lqw);
        List<DishDto> dtoList=list.stream().map((item) ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null){
                //只是需要菜品分类名称
                dishDto.setCategoryName(category.getName());
            }
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(item.getId()!=null,DishFlavor::getDishId,item.getId());
            List<DishFlavor> dishFlavorList= dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dtoList);
    }


    /**
     * 更新售卖状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status,@RequestParam String ids){
      log.info(ids);
        String[] info = ids.split(",");
        ArrayList<Long> list = new ArrayList<>();
        for (String str : info) {
            //转成Long类型
            Long id = Long.valueOf(str);
            Dish dish = dishService.getById(id);
            if (status.equals(dish.getStatus())){
                String err = status == 0?"该菜品已处于停售状态":"该菜品已处于启售状态";
                return R.error(err);
            }
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return R.success("更新成功！");
    }



}
