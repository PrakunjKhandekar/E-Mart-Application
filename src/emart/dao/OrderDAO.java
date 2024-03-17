/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.OrdersPojo;
import emart.pojo.ProductsPojo;
import emart.pojo.UserProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author PRAKUNJ
 */
public class OrderDAO {

    public static String getNextOrderId() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select max(order_id) from orders");
        rs.next();
        String orderId = rs.getString(1);
        if (orderId == null) {
            return "O-101";
        }
        int orderNo = Integer.parseInt(orderId.substring(2));
        orderNo++;
        return "O-" + orderNo;
    }

    public static boolean addOrder(ArrayList<ProductsPojo> al, String orderId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("Insert into orders values(?,?,?,?)");
        int count = 0;
        for (ProductsPojo p : al) {
            ps.setString(1, orderId);
            ps.setString(2, p.getProductId());
            ps.setInt(3, p.getQuantity());
            ps.setString(4, UserProfile.getUserid());
            count += ps.executeUpdate();
        }
        return count == al.size();
    }

    public static Set<String> getAllOrderId() throws SQLException {
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select order_id from orders");
        HashSet<String> allId = new HashSet<>();
        while (rs.next()) {
            allId.add(rs.getString(1));
        }
        return allId;
    }

    public static ArrayList<OrdersPojo> managerOrders(String oid) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT o.p_id, p.p_name, p.p_companyName, p.p_price, p.our_price, p.p_tax, o.quantity FROM orders o JOIN products p ON o.p_id = p.p_id WHERE o.order_id = ?");
        ps.setString(1, oid);
        ResultSet rs = ps.executeQuery();
        ArrayList<OrdersPojo> al = new ArrayList<>();
        while (rs.next()) {
            OrdersPojo order = new OrdersPojo();
            order.setProductId(rs.getString(1));
            order.setProductName(rs.getString(2));
            order.setCompanyName(rs.getString(3));
            order.setProductPrice(rs.getDouble(4));
            order.setOurPrice(rs.getDouble(5));
            order.setTax(rs.getInt(6));
            order.setQuantity(rs.getInt(7));
            al.add(order);
        }
        return al;
    }
    public static ArrayList<OrdersPojo> receptionistOrders(String oid,String userid) throws SQLException {
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT o.p_id, p.p_name, p.p_companyName, p.p_price, p.our_price, p.p_tax, o.quantity FROM orders o JOIN products p ON o.p_id = p.p_id WHERE o.order_id = ? and userid=?");
        ps.setString(1, oid);
        ps.setString(2, userid);
        ResultSet rs = ps.executeQuery();
        ArrayList<OrdersPojo> al = new ArrayList<>();
        while (rs.next()) {
            OrdersPojo order = new OrdersPojo();
            order.setProductId(rs.getString(1));
            order.setProductName(rs.getString(2));
            order.setCompanyName(rs.getString(3));
            order.setProductPrice(rs.getDouble(4));
            order.setOurPrice(rs.getDouble(5));
            order.setTax(rs.getInt(6));
            order.setQuantity(rs.getInt(7));
            al.add(order);
        }
        return al;
    }
}
