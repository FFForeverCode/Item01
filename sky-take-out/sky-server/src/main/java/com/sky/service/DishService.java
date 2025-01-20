package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    //分页查询
    PageResult DishQuery(DishPageQueryDTO dishPageQueryDTO);

    void modifyDish(DishDTO dishDto);
    //新增菜品
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 根据分类ID查询菜品及其口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);


    /**
     * 菜品的批量删除
     * @param ids
     */
    void deleteBach(List<Long> ids);

    void ManageSale(int status, int id);

    /**
     * 根据ID查询菜品及其口味数据
     *
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

}
