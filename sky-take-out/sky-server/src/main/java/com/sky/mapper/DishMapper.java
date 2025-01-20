package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 修改菜品
     * @param dish
     */
    void modifyDish(Dish dish);

    /**
     * 新增菜品
     * @param dish
     */
    void insert(Dish dish);

    /**
     * 分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> DishQuery(DishPageQueryDTO dishPageQueryDTO);
    //根据条件查询
    List<Dish>list(Dish dish);

    @Select("select* from dish where id = #{id}")
    Dish getbyId(Long id);

    void delete(List<Long>ids);

    @Update("update dish set status = #{status} where id = #{id}")
    void ManageSale(int status, int id);

}
