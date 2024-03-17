/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.ReceptionistPojo;
import emart.pojo.UserPojo;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PRAKUNJ
 */
public class ReceptionistDAO {
    public static Map<String,String> getNonRegesteredReceptionists() throws SQLException{
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select empid,empname from employees where job='Receptionist' and empid not in(select empid from users where usertype='Receptionist')");
        HashMap<String,String> recepList = new HashMap<>();
        while(rs.next()){
            String id=rs.getString(1);
            String name=rs.getString(2);
            recepList.put(id,name);
        }
        return recepList;
    }
    public static boolean addReceptionist(UserPojo user) throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into users values(?,?,?,?,?)");
        ps.setString(1,user.getUserid());
        ps.setString(2, user.getEmpid());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getUsertype());
        ps.setString(5, user.getUsername());
        int result = ps.executeUpdate();
        return result==1;
    }
    
    public static List<ReceptionistPojo> getAllReceptionists() throws SQLException{
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select users.empid,empname,userid,job,salary from employees,users where usertype='Receptionist' and users.empid=employees.empid");
        ArrayList<ReceptionistPojo> al = new ArrayList<>();
        while(rs.next()){
            ReceptionistPojo recep = new ReceptionistPojo();
            recep.setEmpId(rs.getString(1));
            recep.setEmpName(rs.getString(2));
            recep.setUserId(rs.getString(3));
            recep.setJob(rs.getString(4));
            recep.setSalary(rs.getDouble(5));
            al.add(recep);
        }
        return al;
    }
    public static Map<String,String> getReceptioinstId()throws SQLException{
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select userid,username from users where usertype='Receptionist' order by userid");
        HashMap<String,String> recepList = new HashMap<>();
        while(rs.next()){
            String id=rs.getString(1);
            String name=rs.getString(2);
            recepList.put(id,name);
        }
        return recepList;
    }
    
    public static boolean updatePassword(String userid,String pwd) throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("update users set password=? where userid=?");
        ps.setString(1,pwd);
        ps.setString(2, userid);
        return ps.executeUpdate()==1;
    }
    
    public static List<String> getAllReceptionistUserId()throws SQLException{
    Connection conn = DBConnection.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("Select userid from users where usertype='Receptionist' order by userid");
    List<String> receptionistList = new ArrayList<>();
    while(rs.next()){
        String id = rs.getString(1);
        receptionistList.add(id);
    }
    return receptionistList;
    }
    
    public static boolean deleteReceptionist(String userid)throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("delete from users where userid=?");
        ps.setString(1,userid);
        return ps.executeUpdate()==1;
    }
}
