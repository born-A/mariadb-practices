package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.CartVo;
import bookmall.vo.CategoryVo;
import bookmall.vo.OrderBookVo;
import bookmall.vo.OrderVo;

public class CategoryDao {
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
	
	public void insert(CategoryVo vo) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("insert into category(name) values(?)");
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
			) {
				pstmt1.setString(1, vo.getName());
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
				PreparedStatement pstmt = conn.prepareStatement("delete from category where no = ?");
			) {
				pstmt.setLong(1, no);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("error:" + e);
		}
	}

	public List<CategoryVo> findAll() {
		List<CategoryVo> result = new ArrayList<>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from category order by no desc");
			ResultSet rs = pstmt.executeQuery();
		) {
			while(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);

				
				CategoryVo vo = new CategoryVo(name);
				vo.setNo(no);
			
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
	}
}
