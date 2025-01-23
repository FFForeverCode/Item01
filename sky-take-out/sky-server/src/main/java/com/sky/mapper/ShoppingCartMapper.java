package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 插入购物车数据
     * @param shoppingCart
     * @return
     */
    @Insert("insert into shopping_cart(name,image,user_id," +
            "dish_id,setmeal_id,dish_flavor," +
            "number,amount,create_time)VALUES (" +
            "#{name},#{image},#{userId},#{dishId},#{setmealId}," +
            "#{dishFlavor},#{number},#{amount},#{createTime})")
    void add(ShoppingCart shoppingCart);

    /**
     * 查询购物车中的商品
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> select(ShoppingCart shoppingCart);

    /**
     * 更新数量
     * @param shoppingCart1
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNum(ShoppingCart shoppingCart1);

    /**
     * 清空购物车
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void delete(Long userId);
}
