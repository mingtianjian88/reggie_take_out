package com.zhang.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.common.R;
import com.zhang.pojo.Category;
import com.zhang.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2023-07-20 15:48
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增操作
     * @param category
     * @return
     */
    @PostMapping
    public R<String> addDishes(@RequestBody Category category){
        //因为有统一的异常处理类，所以不需要对重复名称进行处理
       categoryService.save(category);
       return R.success("保存成功!");
    }


    /**
     * 分页操作
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(@RequestParam Integer page,@RequestParam Integer pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        log.info("page: "+page+", pageSize "+pageSize );
        //根据sort字段来进行排序
        LambdaQueryWrapper<Category> lqw  = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,lqw);
        return R.success(pageInfo);
    }

    /**
     * 更新操作
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功！");
    }


    /**
     * 删除分类操作
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam Long ids){
        categoryService.remove(ids);

        return R.success("成功删除该分类！");
    }


//    /**
//     * 获取菜品分类
//     * @return
//     */  自己写的代码 还是不到位
//    @GetMapping("/list")
//    public R<List> list(@RequestParam Integer type){
//        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Category::getType,type);
//        List<Category>  list = categoryService.list(queryWrapper);
//        return R.success(list);
//    }



    @GetMapping("/list")        //这里为了更好的获取数据，所以用了 category实体类来接收数据
    public R<List<Category>> list( Category category){

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //第一排序 根据sort排序，如果sort相同，根据更新时间来排序
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
