//OrderDetail.java文件
package com.a51work6.jpetstore.domain;

//订单明细实体类
public class OrderDetail {
    private long orderid;        // 订单Id
    private String productid;    // 商品Id
    private int quantity;        // 商品数量
    private double unitcost;     // 单价


    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitcost() {
        return unitcost;
    }

    public void setUnitcost(double unitcost) {
        this.unitcost = unitcost;
    }
}
