package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

/**
 * @author ForeverCode
 * @date 2024/12/26
 */
public interface CategoryService {
    /**
     * 新增分类菜品
     * @param categoryDTO
     */
    void insertCategory(CategoryDTO categoryDTO);

    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void modify(CategoryDTO categoryDTO);

    void UseOrBan(int status, Long id);

    void deleteCategory(Long id);

    PageResult selectByType(int type);
}
