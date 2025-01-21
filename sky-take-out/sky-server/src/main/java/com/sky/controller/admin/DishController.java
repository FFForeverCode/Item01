package com.sky.controller.admin;

import com.sky.annotation.DeleteCache;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.service.impl.DishServiceImpl;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")//TODO:一定要确定好路径
@Api(tags="菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/{id}")
    @ApiOperation("根据ID查询菜品")
    public Result<DishVO>selectById(@PathVariable Long id){
        log.info("开始根据ID查询菜品:{}",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }
    /**
     * 分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ResponseBody
    @ApiOperation("菜品分页查询")
    public Result<PageResult>dishQuery(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询{}",dishPageQueryDTO);
        return Result.success(dishService.DishQuery(dishPageQueryDTO));
    }


    @PutMapping
    @ApiOperation("修改菜品")
    public Result<String>modifyDish(@RequestBody DishDTO dishDto){
        log.info("修改菜品{}",dishDto);
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
        log.info("新增菜品{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("菜品删除")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品的批量删除{}",ids);
        dishService.deleteBach(ids);
        return Result.success();
    }

    /**
     * 禁用或开启菜品出售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result ManageSale(@PathVariable int status,int id){
        log.info("管理菜品是否出售,{}",status);
        dishService.ManageSale(status,id);
        return Result.success();

    }


}
