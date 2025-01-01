package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper {
    //分页
    Page<Dish> DishQuery(DishPageQueryDTO dto);

    @AutoFill(OperationType.UPDATE)
    void modifyDish(Dish dish);

    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);
}
