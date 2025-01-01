package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author ForeverCode
 * @date 2024/12/26
 */
@Mapper
public interface CategoryMapper {

    /**
     * 新增分类
     * @param category
     */
    @AutoFill(OperationType.INSERT)
    public void insertCategory(Category category);

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 修改分类 xml实现
     * @param category
     */
    @AutoFill(OperationType.UPDATE)
    void modify(Category category);


    @AutoFill(OperationType.UPDATE)
    @Update("update category set status = #{status} where id=#{id}")
    void UseOrBan(Category category);

    @Delete("delete from category where id=#{id}")
    void deleteByID(Long id);
}
