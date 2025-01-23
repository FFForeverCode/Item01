package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.impl.ShoppingCartServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C端购物车相关接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartServiceImpl shoppingCartService;

    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCart){
        log.info("添加购物车，商品信息为{}",shoppingCart);
        shoppingCartService.add(shoppingCart);
        return Result.success();
    }
    //GET /user/shoppingCart/list
    @GetMapping("/list")
    @ApiOperation("显示购物车商品")
    public Result<List<ShoppingCart>> list(){
        log.info("查看购物车功能...");
        return Result.success(shoppingCartService.selectAll());
    }

    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result cleanCart(){
        log.info("清空购物车...");
        shoppingCartService.clean();
        return Result.success();
    }
}
