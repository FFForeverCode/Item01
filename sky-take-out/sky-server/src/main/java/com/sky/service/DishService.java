package com.sky.service;

import com.alibaba.fastjson.JSONPatch;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import org.apache.poi.ss.formula.DataValidationEvaluator;
import org.springframework.beans.factory.annotation.Autowired;

public interface DishService {
    //分页查询
    PageResult DishQuery(DishPageQueryDTO dishPageQueryDTO);

    void modifyDish(DishDTO dishDto);
    //新增菜品
    void saveWithFlavor(DishDTO dishDTO);


}
