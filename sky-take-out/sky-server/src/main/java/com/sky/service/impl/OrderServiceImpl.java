package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.BaseException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;
    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {

        log.info("提交订单,{}",ordersSubmitDTO);
        //处理各种业务异常（地址为空、购物车为空）
        AddressBook address = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(address == null){
            throw new BaseException(MessageConstant.ADDRESS_BOOK_IS_NULL);//地址为空
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());//根据用户Id来获取购物车
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.select(shoppingCart);
        if(shoppingCarts==null||shoppingCarts.isEmpty()){
            throw new BaseException(MessageConstant.SHOPPING_CART_IS_NULL);//购物车为空
        }
        //向订单提交1条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);//属性拷贝
        //设置无法拷贝的属性
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(address.getPhone());
        orders.setConsignee(address.getConsignee());
        orders.setUserId(BaseContext.getCurrentId());
        orderMapper.insert(orders);
        //向订单明细提交n条数据 因为一个订单 可以有 多个明细
        List<OrderDetail>orderDetails = new LinkedList<>();
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(orders.getId());//设置订单明细关联的订单ID
            orderDetails.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetails);
        //清空购物车
        shoppingCartMapper.delete(BaseContext.getCurrentId());
        //封装VO结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();

        return orderSubmitVO;
    }
}
