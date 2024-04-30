package util.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnect {
	public Connection conn = null;
	public Statement stmt = null;
	public ResultSet rs = null;
	private String DB_HOST = "localhost";
	private String DB_USER = "testuser";
	private String DB_PW = "test1234";

	// 생성자
	public DbConnect() {
		try {
			Class.forName("oracle.jdbc.OracleDriver"); // 드라이버 로드
			conn = DriverManager.getConnection("jdbc:oracle:thin:@" + DB_HOST + ":1521:xe", DB_USER, DB_PW); // DB 커넥트
//			System.out.println("DB 접속 성공");
			stmt = conn.createStatement(); // sql 실행할 객체(statement)
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 쿼리 실행
	public ResultSet execute(PreparedStatement pstmt) {
		try {
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public int update(PreparedStatement pstmt) { // insert, update, delete
		int rows = 0;
		try {
			rows = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}
//
//	// 쿼리 실행
//	public ResultSet select(String query) {
//		try {
//			rs = pstmt.executeQuery();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return rs;
//	}

	// 자원 해제
	public void close() {
		try {
			stmt.close();
		} catch (SQLException e) {
		}
		try {
			conn.close();
		} catch (SQLException e) {
		}
		try {
			rs.close();
		} catch (SQLException e) {
		}
		System.out.println("접속 종료");
	}
}
