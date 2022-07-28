package Login_pkg;

import java.sql.*;

public class LoginDB {
	private Connection conn = null;
	private ResultSet rs = null;
	private Statement st = null;
	private PreparedStatement ps = null;
	int count = 0;
	
	
	public LoginDB() { // �����ڷ� ������ ���̽� ����
		
		final String url = "jdbc:mariadb://localhost:3306/puzzledb";
		final String id = "root";
		final String pw = "1234";
		try{
		Class.forName("org.mariadb.jdbc.Driver");
		conn = DriverManager.getConnection(url, id, pw);
		}catch(ClassNotFoundException cnfe) {
			System.out.println("DB ����̹� �ε� ����:"+ cnfe.toString());
		}catch(SQLException sqle){
			System.out.println("DB ���ӽ���"+ sqle.toString());
		}catch(Exception e){
			System.out.println("Unkown error");
			e.printStackTrace();
		}	
	}

	
	public void DBClose(){ // Ŀ�ؼ� ���� ����
		try{
			if(rs != null) rs.close();
			if(st != null) st.close();
			if(ps != null) ps.close();
		}catch(Exception e) { System.out.println(e + "=> DBClose ����");}
	}
	
	// ȸ�� ���� ����
	public void InsertLogin(LoginData logindata){  // login table -> Login data insert
		String sql = "insert into login values(?, ?, ?)";
		
		try{
			ps = conn.prepareStatement(sql);
			st = conn.createStatement();
			rs = st.executeQuery("Select * From login order by position*1");
			while(rs.next())
			{
				int Number1 = rs.getInt("position");
				if(Number1 == count) count++;
				}
			ps.setInt(1, count);
			ps.setString(2, logindata.GetID());
			ps.setString(3, logindata.GetPassword());
			ps.executeUpdate();
			count++;
		}catch(SQLException e) 
		{
			e.printStackTrace();
		}finally 
		{
			DBClose();
		}
	}
	
	// ȸ�� ���� ����
	public void Delete(String ID){  // login table -> Login data delete
		String sql = "Delete from login where ID =?";
		
		try{
			ps = conn.prepareStatement(sql);
			ps.setString(1, ID);
			ps.executeUpdate();
		}catch(SQLException e)
		{	
			e.printStackTrace();
		} finally 
		{
			DBClose();
		}
	}
	
	// ID, Password Ȯ��
	public int LoginTry(LoginData logindata){ // login table -> Login ID, Password Confirm
		String sql = "select * from login where ID = ? and Password = ?";
		
		try{
		ps = conn.prepareStatement(sql);
		ps.setString(1, logindata.GetID());
		ps.setString(2, logindata.GetPassword());
		rs = ps.executeQuery();
		
		if(rs.next())
		{
			return 1;		
		}	
		}catch(Exception e) 
		{
			e.printStackTrace();
		}
		return -1;
	}
	
	// ID Ȯ��
	public int LoginOX(LoginData logindata){ // login_management table -> Login ID Confirm
		String sql = "select * from login where ID = ?";
		try{
		ps = conn.prepareStatement(sql);
		ps.setString(1, logindata.GetID());
		rs = ps.executeQuery();
		if(rs.next()) 
		{
			return 1;
		}
		
		}catch(Exception e)
		{ 
			e.printStackTrace();
		}
		return -1;
	}

}

