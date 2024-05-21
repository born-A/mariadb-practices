package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.BookVo;
import bookmall.vo.OrderVo;

public class BookDao {
	private Connection getConnection() throws SQLException {
		Connection conn = null;
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.64.4:3306/bookmall?charset=utf8";
			conn = DriverManager.getConnection(url, "bookmall", "bookmall");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}
		
		return conn;
	}
	
	public void insert(BookVo vo) {
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("insert into book(title, price, category_no) values(?, ?, ?)");

			PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		) {
			pstmt1.setString(1, vo.getTitle());
			pstmt1.setInt(2, vo.getPrice());
			pstmt1.setLong(3, vo.getCategoryNo());
			pstmt1.executeUpdate();
			
			ResultSet rs = pstmt2.executeQuery();
			rs.next();
			vo.setNo(rs.getLong(1));
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}	
	}



	public void deleteByNo(Long no) {
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from book where no = ?");
		) {
			pstmt.setLong(1, no);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} 
	}

}