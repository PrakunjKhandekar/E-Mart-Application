/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emart.dao;

import emart.dbutil.DBConnection;
import emart.pojo.EmployeePojo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PRAKUNJ
 */
public class EmployeesDAO {
    public static String grtNextEmpId() throws SQLException{
        Connection conn = DBConnection.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("Select max(empid) from employees");
        rs.next();
        String empid=rs.getString(1);
        int empno=Integer.parseInt(empid.substring(1));
        empno++;
        return "E"+empno;
    }
    public static boolean addEmployee(EmployeePojo emp)throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement st = conn.prepareStatement("Insert into employees values(?,?,?,?)");
        st.setString(1,emp.getEmpid());
        st.setString(2,emp.getEmpname());
        st.setString(3,emp.getJob());
        st.setDouble(4,emp.getSalary());
        int result=st.executeUpdate();
        return result==1;
    }
    
    public static List<EmployeePojo> getAllEmployees()throws SQLException{
     Connection conn = DBConnection.getConnection();
     Statement st = conn.createStatement();
     ResultSet rs = st.executeQuery("Select * from employees order by empid");
     ArrayList <EmployeePojo> empList = new ArrayList<>();
     while(rs.next()){
         EmployeePojo emp = new EmployeePojo();
         emp.setEmpid(rs.getString(1));
         emp.setEmpname(rs.getString(2));
         emp.setJob(rs.getString(3));
         emp.setSalary(rs.getDouble(4));
         empList.add(emp);
     }
     return empList;
    }
    
    public static List<String> getAllEmpId()throws SQLException{
     Connection conn = DBConnection.getConnection();
     Statement st = conn.createStatement();
     ResultSet rs = st.executeQuery("Select empid from employees order by empid");
     ArrayList<String> allId = new ArrayList<>();
     while(rs.next()){
         allId.add(rs.getString(1));
     }
     return allId;
    }
    
    public static EmployeePojo getEmpById(String empid) throws SQLException{
     Connection conn = DBConnection.getConnection();   
     PreparedStatement ps = conn.prepareStatement("Select * from employees where empid=(?)");
     ps.setString(1,empid);
     ResultSet rs = ps.executeQuery();
     rs.next();
     EmployeePojo e = new EmployeePojo();
     e.setEmpid(rs.getString(1));
     e.setEmpname(rs.getString(2));
     e.setJob(rs.getString(3));
     e.setSalary(rs.getDouble(4));
     return e;
    } 
    
    public static boolean updateEmployee(EmployeePojo emp)throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("update employees set empname=(?),job=(?),salary=(?) where empid=(?)");
        ps.setString(1,emp.getEmpname());
        ps.setString(2,emp.getJob());
        ps.setDouble(3,emp.getSalary());
        ps.setString(4,emp.getEmpid());
        int result=ps.executeUpdate();
        if(result==0){
            return false;
        }
        else{
            boolean res = UserDAO.isUserPresent(emp.getEmpid());
            if (res==false)
            return true;
            ps=conn.prepareStatement("update users set username=?,usertype=? where empid=?");
            ps.setString(1, emp.getEmpname());
            ps.setString(2, emp.getJob());
            ps.setString(3, emp.getEmpid());
            int y=ps.executeUpdate();
            return y==1;
        }
    }
    public static boolean deleteEmployee(EmployeePojo emp)throws SQLException{
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("delete from employees where empid=(?)");
        ps.setString(1,emp.getEmpid());
        int result=ps.executeUpdate();
        if(result==0){
            return false;
        }
        else{
            boolean res = UserDAO.isUserPresent(emp.getEmpid());
            if (res==false)
            return true;
            ps=conn.prepareStatement("delete from users where empid=?");
            ps.setString(1, emp.getEmpid());
            int y=ps.executeUpdate();
            return y==1;
        }
    }
}
