package com.zhang.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.common.R;
import com.zhang.pojo.Category;
import com.zhang.pojo.Setmeal;
import com.zhang.pojo.SetmealDish;
import com.zhang.pojo.SetmealDto;
import com.zhang.service.CategoryService;
import com.zhang.service.SetmealDishService;
import com.zhang.service.SetmealDtoService;
import com.zhang.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shkstart
 * @create 2023-07-22 21:37
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐操作
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        //套餐 setmeal -->  子类setmealDto 含有setmealDish集合属性
        //套餐以及其菜品信息：setmealDish
        //保存套餐的信息
        setmealService.save(setmealDto);

        // 保存套餐中对应的菜品信息
        List<SetmealDish> list = setmealDto.getSetmealDishes();
        // 数据中没有setmeal_dishID的值，所有我们要给集合中的每一条setmealdish中 的数据赋setmeal_dishID属性
        list = list.stream().map((item) ->{
            Long id = setmealDto.getId();
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(list);
        return R.success("保存套餐成功！");
    }


    /**
     * 分页操作
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        //缺少套餐分类 categoryName，这个属性在SetmealDto中
        Page<Setmeal> pageInfo = new Page<>();
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(name != null,Setmeal::getName,name);
        setmealService.page(pageInfo,lqw);
        Page<SetmealDto> dtoPage = new Page<>();
        //使用工具类 拷贝一份pageInfo中的数据到dtoPage中,除了源数据records
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        //从pageInfo中得到源数据
        List<Setmeal> list = pageInfo.getRecords();
        List<SetmealDto> dtoList = list.stream().map((item) ->{
            SetmealDto setmealDto = new SetmealDto();
            //因为这里的setmealDto是我们new出来的，所以是没有setmeal的数据的，所以将itme中的数据拷贝到setmealDto中
            BeanUtils.copyProperties(item,setmealDto);
            //通过源数据中的 setmeal对象获取到套餐分类ID
            Long categoryId = item.getCategoryId();
            //根据菜品ID来获取套餐分类名称
            String categoryName = categoryService.getById(categoryId).getName();
            //给创建的setmealDto对象赋值套餐分类名称
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());
       dtoPage.setRecords(dtoList);
        return R.success(dtoPage);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping       //这里的多个数据可以用一个List来接收
    public R<String> delete(@RequestParam List<Long> ids){
        // 原来自己写的代码是用String ids 来接收数据，但是这样效率很低
//        String[] idArr = ids.split(",");
//        //将字符串转化为Long类型
//        for (String s : idArr) {
//            Long id = Long.valueOf(s);
//            if (id != 0) {
//                setmealService.removeById(id);
//                LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
//                lqw.eq(SetmealDish::getSetmealId,id);
//                setmealDishService.remove(lqw);
//            }else {
//                return R.error("没有找到该套餐！");
//            }
//        }
//        return R.success("删除成功！");

        //select count(*) from steaml in id = (...) where status = 1
        //不能删除启售状态的套餐
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = setmealService.count(queryWrapper);
        if (count > 0){
            return R.error("该套餐处于启售状态，不能删除");
        }
        setmealService.removeByIds(ids);

        //删除对应的setmealDish
        //  delete from setmeal_dish in id = (...)
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(lqw);
        return R.success("删除成功！");
    }

    /**
     * 更新售卖状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status,@RequestParam List<Long> ids){
        //select * from setmeal where id in(...)
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId,ids);
        List<Setmeal> list = setmealService.list(lqw);
            list.stream().map((item)->{
            if (status.equals(item.getStatus())){
                String err = status == 0?"该套餐已处于停售状态":"该套餐已处于启卖状态";
                return R.error(err);
            }
            item.setStatus(status);
            return item;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(list);
        return R.success("停售成功");
    }

    @GetMapping("list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        //根据 categoryId 查对应的 套餐 setmeal
         LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
         lqw.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
         lqw.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> setmeals = setmealService.list(lqw);
        return R.success(setmeals);
    }
}
