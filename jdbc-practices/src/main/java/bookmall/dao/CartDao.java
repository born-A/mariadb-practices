package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.CartVo;
import bookmall.vo.OrderBookVo;
import bookmall.vo.OrderVo;

public class CartDao {
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
	
	public void insert(CartVo vo) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("insert into cart(user_no, book_no, quantity) values(?, ?, ?)");
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
			) {
				pstmt1.setLong(1, vo.getUserNo());
				pstmt1.setLong(2, vo.getBookNo());
				pstmt1.setInt(3, vo.getQuantity());
				pstmt1.executeUpdate();
				
				ResultSet rs = pstmt2.executeQuery();
				rs.close();
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
	}

	public List<CartVo> findByUserNo(Long userNo) {
		List<CartVo> result = new ArrayList<>();
		try (
			Connection connection = getConnection();
			PreparedStatement pstmt = connection.prepareStatement("SELECT a.book_no, a.quantity, b.title " +
		               "FROM cart a " +
		               "JOIN book b ON a.book_no = b.no " +
		               "WHERE a.user_no = ? ");
					
			) {
			pstmt.setLong(1, userNo);
			ResultSet rs = pstmt.executeQuery();	
			while(rs.next()) {
			Long bookNo = rs.getLong(1);
			int quantity = rs.getInt(2);
			String title = rs.getString(3);
			CartVo vo = new CartVo();
			vo.setBookNo(bookNo);
			vo.setQuantity(quantity);
			vo.setBookTitle(title);
	
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		return result;
	}

	public void deleteByUserNoAndBookNo(Long userNo, Long bookNo) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from cart where user_no = ? and book_no = ?");
			) {
				pstmt.setLong(1, userNo);
				pstmt.setLong(2, bookNo);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("error:" + e);
		}
	}
}
