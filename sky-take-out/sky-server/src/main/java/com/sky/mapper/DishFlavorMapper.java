package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味表
     * @param dishFlavors
     */
    void insertBath(List<DishFlavor> dishFlavors);

    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    void deleteByDishIds(List<Long> ids);

    @Select("select* from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);
}
