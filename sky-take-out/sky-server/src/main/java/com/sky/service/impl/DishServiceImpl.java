package com.sky.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper flavorMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    public PageResult DishQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<Dish>page = dishMapper.DishQuery(dishPageQueryDTO);
        long total = page.getTotal();
        List<Dish> dishes = page.getResult();
        return new PageResult(total,dishes);
    }

    /**
     * 修改菜品
     * @param dishDto
     */
    @Override
    public void modifyDish(DishDTO dishDto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        dishMapper.modifyDish(dish);
    }

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    @Transactional//事务管理，多个表的操作，需要保证数据一致性
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();//Dish类中没有口味表
        BeanUtils.copyProperties(dishDTO,dish);
        //向菜品中插入1条数据
        dishMapper.insert(dish);
        //Long id = dish.getId();获取不了，因为id在Dish表中是自增，自己产生的
        Long id = dish.getId();//此时可以获取菜品主键值
        //因此需要在插入操作时，设置UseGeneratedKeys = true keyProperties = id
        //获取口味表
        List<DishFlavor>dishFlavors = dishDTO.getFlavors();
        if(dishFlavors!=null&& !dishFlavors.isEmpty()){
            //设置菜品id forEach()方法
            dishFlavors.forEach(dishFlavor ->{
                dishFlavor.setDishId(id);
            });
            //口味表中插入n条数据
            dishFlavorMapper.insertBath(dishFlavors);
        }
    }

}
