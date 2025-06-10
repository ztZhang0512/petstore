package com.a51work6.jpetstore.dao;

import com.a51work6.jpetstore.domain.Order;

import java.util.List;

//订单管理DAO
public interface OrderDao {
    // 查询所有的订单信息
    List<Order> findAll();
    // 根据主键查询订单信息
    Order findById(long orderid);
    // 创建订单信息
    int create(Order order);
    // 修改订单信息
    int modify(Order order);
    // 删除订单信息
    int remove(Order order);

    List<Order> findPendingOrdersByUser(String userid);

    List<Order> findHistoryOrdersByUser(String userid);
}
