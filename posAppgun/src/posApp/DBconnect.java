package posApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnect {

	private static final String dbDriver = "com.mysql.cj.jdbc.Driver";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/pos?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF8";
	private static final String id = "root";
	private static final String pw = "1234";
	private static Connection conn = null;
	
//	public static void main(String[] args) throws InterruptedException {}

	public static Connection connect() {
		try {
			// 1. 드라이버 로드
			Class.forName(dbDriver);
			// 2. 연결객체
			conn = DriverManager.getConnection(dbUrl, id, pw);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return conn;

	}

	public static void close() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("DB close() 에러!");
		}
	}
}

