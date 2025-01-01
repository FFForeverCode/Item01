package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/dish")//TODO:一定要确定好路径
@Api(tags="菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @GetMapping("/page")
    @ResponseBody
    @ApiOperation("菜品分页查询")
    public Result<PageResult>dishQuery(@RequestBody DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询{}",dishPageQueryDTO);
        return Result.success(dishService.DishQuery(dishPageQueryDTO));
    }


    @PutMapping
    @ApiOperation("修改菜品")
    public Result<String>modifyDish(@RequestBody DishDTO dishDto){
        //代码
        dishService.modifyDish(dishDto);


        return Result.success();
    }

    /**
     * 添加菜品
     * @return
     */
    @SuppressWarnings("all")
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        return Result.success();
    }


}
