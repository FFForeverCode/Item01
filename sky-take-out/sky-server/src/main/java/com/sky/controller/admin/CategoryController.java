package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ForeverCode
 * @date 2024/12/26
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类管理控制层")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param categoryDTO 新增分类对象
     * @return
     */
    @PostMapping
    @ResponseBody
    @ApiOperation("新增分类")
    public Result<String>insertCategory(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类:{}",categoryDTO);
        categoryService.insertCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 分页查询,根据条件
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ResponseBody
    @ApiOperation("分类分页查询")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("员工分页查询:{}",categoryPageQueryDTO);
        return Result.success(categoryService.pageQuery(categoryPageQueryDTO));
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ResponseBody
    @ApiOperation("修改分类")
    public Result<String>modify(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类{}",categoryDTO);
        categoryService.modify(categoryDTO);
        return Result.success();
    }

    /**
     * 启用或禁用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用分类")
    public Result<String> UseOrBan(@PathVariable int status,Long id){
        log.info("启用或禁用分类:{}",status);
        categoryService.UseOrBan(status,id);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public Result<String>deleteCategory(Long id){
        log.info("删除分类:{}",id);
        categoryService.deleteCategory(id);
        return Result.success();
    }
    //TODO:弃用
    @GetMapping("/list")
    @ResponseBody
    public Result<PageResult>selectByType(int type){
        return Result.success(categoryService.selectByType(type));
    }



}
