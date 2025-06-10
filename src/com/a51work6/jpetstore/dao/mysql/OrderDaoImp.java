// OrderDaoImp.java文件
package com.a51work6.jpetstore.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.a51work6.jpetstore.dao.OrderDao;
import com.a51work6.jpetstore.domain.Order;

// 订单管理DAO
public class OrderDaoImp implements OrderDao {

    @Override
    public List<Order> findAll() {
        String sql = "SELECT orderid, userid, orderdate, status, amount, isdel FROM orders WHERE isdel = 0";
        List<Order> orderList = new ArrayList<>();

        try (
                Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
        ) {
            while (rs.next()) {
                Order order = new Order();
                order.setOrderid(rs.getLong("orderid"));
                order.setUserid(rs.getString("userid"));
                order.setOrderdate(rs.getTimestamp("orderdate"));
                order.setStatus(rs.getInt("status"));
                order.setAmount(rs.getDouble("amount"));
                order.setIsdel(rs.getInt("isdel"));

                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    @Override
    public List<Order> findPendingOrdersByUser(String userid) {
        String sql = "SELECT orderid, userid, orderdate, status, amount, isdel FROM orders WHERE userid = ? AND isdel = 0";
        List<Order> orderList = new ArrayList<>();

        try (
                Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderid(rs.getLong("orderid"));
                order.setUserid(rs.getString("userid"));
                order.setOrderdate(rs.getTimestamp("orderdate"));
                order.setStatus(rs.getInt("status"));
                order.setAmount(rs.getDouble("amount"));
                order.setIsdel(rs.getInt("isdel"));

                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    @Override
    public List<Order> findHistoryOrdersByUser(String userid) {
        String sql = "SELECT orderid, userid, orderdate, status, amount, isdel FROM orders WHERE userid = ? AND status = 1 AND isdel = 0";
        List<Order> orderList = new ArrayList<>();

        try (
                Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderid(rs.getLong("orderid"));
                order.setUserid(rs.getString("userid"));
                order.setOrderdate(rs.getTimestamp("orderdate"));
                order.setStatus(rs.getInt("status"));
                order.setAmount(rs.getDouble("amount"));
                order.setIsdel(rs.getInt("isdel"));

                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }


    @Override
    public int create(Order order) {
        String sql = "INSERT INTO orders (orderid, userid, orderdate, status, amount, isdel) VALUES (?, ?, ?, ?, ?, ?)";
        try (
                // 2. 创建数据库连接
                Connection conn = DBHelper.getConnection();
                // 3. 创建语句对象
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 4. 绑定参数
            pstmt.setLong(1, order.getOrderid());
            pstmt.setString(2, order.getUserid());
            pstmt.setTimestamp(3, new java.sql.Timestamp(order.getOrderdate().getTime()));
            pstmt.setInt(4, order.getStatus());
            pstmt.setDouble(5, order.getAmount());
            pstmt.setInt(6, 0); // 默认未删除

            // 5. 执行插入操作
            int affectedRows = pstmt.executeUpdate();
            System.out.printf("成功插入%d条数据。\n", affectedRows);

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public Order findById(long orderid) { // 改为 long
        String sql = "SELECT orderid, userid, orderdate, status, amount, isdel FROM orders WHERE orderid = ?";
        try (
                Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, orderid); // 使用 setLong
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Order order = new Order();
                order.setOrderid(rs.getLong("orderid"));
                order.setUserid(rs.getString("userid"));
                order.setOrderdate(rs.getTimestamp("orderdate"));
                order.setStatus(rs.getInt("status"));
                order.setAmount(rs.getDouble("amount"));
                order.setIsdel(rs.getInt("isdel"));

                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public int modify(Order order) {
        String sql = "UPDATE orders SET userid=?, orderdate=?, status=?, amount=?, isdel=? WHERE orderid=?";
        try (
                Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, order.getUserid());
            pstmt.setTimestamp(2, new java.sql.Timestamp(order.getOrderdate().getTime()));
            pstmt.setInt(3, order.getStatus());
            pstmt.setDouble(4, order.getAmount());
            pstmt.setInt(5, order.getIsdel());
            pstmt.setLong(6, order.getOrderid()); // 使用 setLong

            int affectedRows = pstmt.executeUpdate();
            System.out.printf("成功更新%d条数据。\n", affectedRows);
            return affectedRows;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }



    @Override
    public int remove(Order order) {
        String sql = "UPDATE orders SET isdel = 1 WHERE orderid = ?";
        try (
                Connection conn = DBHelper.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, order.getOrderid());
            int affectedRows = pstmt.executeUpdate();

            System.out.printf("成功软删除%d条数据。\n", affectedRows);
            return affectedRows; // ✅ 返回影响行数
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


}
