package com.sky.service.impl;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.context.BaseContext;
import com.sky.controller.user.ShoppingCartController;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());//用户ID
        //查询一下购物车，若购物车中已存在，update 数量+1
        List<ShoppingCart> select = shoppingCartMapper.select(shoppingCart);
        log.info("查询购物车中是否已存在该商品:{}",select!=null&&!select.isEmpty()?"存在":"不存在");
        if(select!=null&&!select.isEmpty()){
            ShoppingCart shoppingCart1 = select.get(0);
            shoppingCart1.setNumber(shoppingCart1.getNumber()+1);
            //update shopping_cart set number = ? where id = ?
            log.info("存在则数量+1");
            shoppingCartMapper.updateNum(shoppingCart1);
        }else{
            //不存在 insert 插入数据
            log.info("不存在，则新增商品");
            //完善商品数据
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();
            if(dishId!=null){
                //商品为菜品
                Dish dish = dishMapper.getbyId(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            }else{
                //商品为套餐
                Setmeal setmeal = setMealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            //不同的用户有不同的购物车，userid
            //shoppingCartMapper.add();
            shoppingCartMapper.add(shoppingCart);
        }

    }

    public List<ShoppingCart> selectAll() {
        Long userId = BaseContext.getCurrentId();
        log.info("查看用户:{}的购物车",userId);
        ShoppingCart shoppingCart= new ShoppingCart();
        shoppingCart.setUserId(userId);
        return shoppingCartMapper.select(shoppingCart);
    }

    public void clean() {
        Long userId = BaseContext.getCurrentId();
        log.info("清空用户:{}的购物车",userId);
        shoppingCartMapper.delete(userId);
    }
}
