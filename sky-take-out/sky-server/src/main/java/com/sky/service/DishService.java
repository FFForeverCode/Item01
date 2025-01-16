package com.sky.service;

import com.alibaba.fastjson.JSONPatch;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.apache.poi.ss.formula.DataValidationEvaluator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface DishService {
    //分页查询
    PageResult DishQuery(DishPageQueryDTO dishPageQueryDTO);

    void modifyDish(DishDTO dishDto);
    //新增菜品
    void saveWithFlavor(DishDTO dishDTO);


    /**
     * 菜品的批量删除
     * @param ids
     */
    void deleteBach(List<Long> ids);

    void ManageSale(int status, int id);

    /**
     * 根据ID查询菜品及其口味数据
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

}
