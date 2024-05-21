package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.OrderBookVo;
import bookmall.vo.OrderVo;

public class OrderDao {
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
	
	public void insert(OrderVo vo) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("insert into orders(number,payment, shipping, status, user_no) values(?,?,?,?,?)");
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
			) {
				pstmt1.setString(1, vo.getNumber());
				pstmt1.setInt(2, vo.getPayment());
				pstmt1.setString(3, vo.getShipping());
				pstmt1.setString(4, vo.getStatus());
				pstmt1.setLong(5, vo.getUserNo());
				pstmt1.executeUpdate();
				
				ResultSet rs = pstmt2.executeQuery();
				rs.next();
				vo.setNo(rs.getLong(1));
				rs.close();
			} catch (SQLException e) {
				System.out.println("error:" + e);
		}
	}

	public void insertBook(OrderBookVo vo) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("insert into orders_book(order_no, book_no, quantity, price) values(?,?,?,?)");
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
			) {
				pstmt1.setLong(1, vo.getOrderNo());
				pstmt1.setLong(2, vo.getBookNo());
				pstmt1.setInt(3, vo.getQuantity());
				pstmt1.setInt(4, vo.getPrice());
				pstmt1.executeUpdate();
				
				ResultSet rs = pstmt2.executeQuery();
				rs.next();
				rs.close();
			} catch (SQLException e) {
				System.out.println("error:" + e);
		}
	}

	public OrderVo findByNoAndUserNo(Long orderNo, Long userNo) {
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"SELECT number, payment, status, shipping, user_no " +
				               "FROM orders " +
				               "WHERE no = ? AND user_no = ?"
					); 
			
		) {
			pstmt.setLong(1, orderNo);
		    pstmt.setLong(2, userNo);
		    
		    try (ResultSet rs = pstmt.executeQuery()) {
		        if (rs.next()) {
		          

		        	String number = rs.getString(1);
		        	int payment = rs.getInt(2);
		        	String status = rs.getString(3);
		        	String shipping = rs.getString(4);
		        	Long userNum = rs.getLong(5);
			
		        	OrderVo vo = new OrderVo(number,payment,shipping,status,userNum);
		        	return vo;
		        }
		    }catch (SQLException e) {
		        e.printStackTrace();
		    }
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return null;
	}

	public List<OrderBookVo> findBooksByNoAndUserNo(Long orderNo, Long userNo) {
	    List<OrderBookVo> result = new ArrayList<>();

	    try (
	        Connection conn = getConnection();
	        PreparedStatement pstmt = conn.prepareStatement(
	        		"SELECT a.order_no, a.quantity, a.price, a.book_no, b.title " +
	                        "FROM orders_book a " +
	                        "JOIN book b ON a.book_no = b.no " +
	                        "JOIN orders c ON a.order_no = c.no " +
	                        "WHERE a.order_no = ? AND c.user_no = ? "
	         );
	    ) {
	        pstmt.setLong(1, orderNo);
	        pstmt.setLong(2, userNo);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Long orderNum = rs.getLong(1);
	                int quantity = rs.getInt(2);
	                int price = rs.getInt(3);
	                Long bookNum = rs.getLong(4);
	                String title = rs.getString(5);

	                OrderBookVo vo = new OrderBookVo();
	                vo.setOrderNo(orderNum);
	                vo.setQuantity(quantity);
	                vo.setPrice(price);
	                vo.setBookNo(bookNum);
	                vo.setBookTitle(title);

	                result.add(vo);
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("error:" + e);
	    }

	    return result;
	}

	public void deleteBooksByNo(Long no) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from orders_book where order_no = ?");
			) {
				pstmt.setLong(1, no);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("error:" + e);
		} 
	}

	public void deleteByNo(Long no) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from orders where no = ?");
			) {
				pstmt.setLong(1, no);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("error:" + e);
		} 
	}
}
