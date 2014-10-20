package com.stackoverflow.utils;
import java.sql.*;

public class DBConnection {
	private static final String DBDrive ="com.mysql.jdbc.Driver";
	private static final String DBUrl="jdbc:mysql://10.131.252.160:3306/stackoverflowdata";
	private static final String DBUser="root";
	private static final String DBPassword="1234";
	public static Connection getConnection() throws Exception
	{
		Connection conn=null;
		try{
			Class.forName(DBDrive).newInstance();
		    conn = DriverManager.getConnection(DBUrl,DBUser, DBPassword);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void close(Connection conn){
		if(conn!=null){
			try{
				conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void close(PreparedStatement pstmt){
		if(pstmt!=null){
			try{
				pstmt.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void close(ResultSet rs){
		if(rs!=null){
			try{
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
}
