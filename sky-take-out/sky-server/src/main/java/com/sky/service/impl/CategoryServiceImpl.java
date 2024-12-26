package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.PageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ForeverCode
 * @date 2024/12/26
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void insertCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setStatus(0);
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.insertCategory(category);
    }

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        long total = page.getTotal();
        List<Category> result = page.getResult();
        return new PageResult(total,result);
    }

    @Override
    public void modify(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        categoryMapper.modify(category);
    }

    @Override
    public void UseOrBan(int status, Long id) {
        categoryMapper.UseOrBan(status,id);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryMapper.deleteByID(id);
    }

    @Override
    public PageResult selectByType(int type) {
        CategoryPageQueryDTO categoryPageQueryDTO = new CategoryPageQueryDTO();
        categoryPageQueryDTO.setType(type);
        categoryPageQueryDTO.setPageSize(PageConstant.PER_PAGE_SIZE);
        return null;
    }
}
