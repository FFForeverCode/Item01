package com.sky.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealMapper setmealMapper;

    /**
     * 分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult DishQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO>page = dishMapper.DishQuery(dishPageQueryDTO);
        long total = page.getTotal();
        List<DishVO> dishes = page.getResult();
        return new PageResult(total,dishes);
    }

    /**
     * 修改菜品
     * @param dishDto
     */
    @Override
    @AutoFill(OperationType.UPDATE)
    public void modifyDish(DishDTO dishDto) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        dishMapper.modifyDish(dish);//修改基本信息
        //更新口味信息
        List<DishFlavor>flavors = dishDto.getFlavors();
        if(flavors!=null && !flavors.isEmpty()) {//注意菜品有可能没有口味
            dishFlavorMapper.deleteByDishId(dish.getId());//先删除旧的口味
            dishFlavorMapper.insertBath(flavors);//再插入新的口味
        }
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

    @Transactional
    @Override
    public void deleteBach(List<Long> ids) {
        //判断当前菜品是否能够删除：
        //在售，则不能删除
        for (Long id : ids) {
            Dish dish = dishMapper.getbyId(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                //在售中，抛出异常，无法删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //若被套餐关联，则不能删除
        List<Long>setMealIds = setmealMapper.getSetMealByDishIds(ids);
        if(setMealIds!=null && !setMealIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品数据
        dishMapper.delete(ids);
        //口味数据删掉
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 禁用或启用菜品
     * @param status
     * @param id
     */
    @Override
    public void ManageSale(int status, int id) {
        dishMapper.ManageSale(status,id);
    }

    /**
     * 菜品（带有口味） 回显
     * @param id
     * @return
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
       //根据id查询菜品数据
        Dish dish = dishMapper.getbyId(id);
        //查询口味数据
        List<DishFlavor>flavors = dishFlavorMapper.getByDishId(id);
        //将两者封装
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

}
