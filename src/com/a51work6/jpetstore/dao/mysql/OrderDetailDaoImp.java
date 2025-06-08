// OrderDetailDaoImp.java文件
package com.a51work6.jpetstore.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.a51work6.jpetstore.dao.OrderDetailDao;
import com.a51work6.jpetstore.domain.OrderDetail;

// 订单明细管理DAO
public class OrderDetailDaoImp implements OrderDetailDao {

    @Override
    public List<OrderDetail> findAll() {
        String sql = "SELECT orderid, productid, quantity, unitcost, isdel FROM orderdetails";
        List<OrderDetail> list = new ArrayList<>();

        try (
                Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderid(rs.getInt("orderid"));
                detail.setProductid(rs.getString("productid"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setUnitcost(rs.getDouble("unitcost"));
                detail.setIsdel(rs.getInt("isdel")); // 新增字段赋值

                list.add(detail);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public OrderDetail findByPK(int orderid, String productid) {
        String sql = "SELECT orderid, productid, quantity, unitcost, isdel FROM orderdetails WHERE orderid = ? AND productid = ?";
        try (
                Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderid);
            pstmt.setString(2, productid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderid(rs.getInt("orderid"));
                detail.setProductid(rs.getString("productid"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setUnitcost(rs.getDouble("unitcost"));
                detail.setIsdel(rs.getInt("isdel"));

                return detail;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int create(OrderDetail orderDetail) {
        String sql = "INSERT INTO orderdetails (orderid, productid, quantity, unitcost, isdel) VALUES (?, ?, ?, ?, ?)";
        try (
                Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, orderDetail.getOrderid());
            pstmt.setString(2, orderDetail.getProductid());
            pstmt.setInt(3, orderDetail.getQuantity());
            pstmt.setDouble(4, orderDetail.getUnitcost());
            pstmt.setInt(5, 0); // 默认未删除

            int affectedRows = pstmt.executeUpdate();
            System.out.printf("成功插入%d条数据。\n", affectedRows);

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public int modify(OrderDetail orderDetail) {
        String sql = "UPDATE orderdetails SET quantity=?, unitcost=?, isdel=? WHERE orderid=? AND productid=?";
        try (
                Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderDetail.getQuantity());
            pstmt.setDouble(2, orderDetail.getUnitcost());
            pstmt.setInt(3, orderDetail.getIsdel());
            pstmt.setLong(4, orderDetail.getOrderid()); // 改用 setLong
            pstmt.setString(5, orderDetail.getProductid());

            int affectedRows = pstmt.executeUpdate();
            System.out.printf("成功更新%d条数据。\n", affectedRows);

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }


    @Override
    public int remove(OrderDetail orderDetail) {
        String sql = "UPDATE orderdetails SET isdel = 1 WHERE orderid = ? AND productid = ?";
        try (
                Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, orderDetail.getOrderid()); // 改用 setLong
            pstmt.setString(2, orderDetail.getProductid());

            int affectedRows = pstmt.executeUpdate();
            System.out.printf("成功软删除%d条数据。\n", affectedRows);

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

}
