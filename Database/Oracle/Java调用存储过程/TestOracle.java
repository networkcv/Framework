package demo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import org.junit.Test;

public class TestOracle {
/*
 * create or replace procedure queryEmpInformation(eno in number,
                                                pename out varchar2,
                                                psal   out number,
                                                pjob   out varchar2)
 */
	@Test
	public void testProcedure(){
		//{call <procedure-name>[(<arg1>,<arg2>, ...)]}
		String sql = "{call queryEmpInformation(?,?,?,?)}";
		
		Connection conn = null;
		CallableStatement call = null;
		try {
			conn = JDBCUtils.getConnection();
			call = conn.prepareCall(sql);
			
			//对于in参数，赋值
			call.setInt(1,7839);
			
			//对于out参数，申明
			call.registerOutParameter(2, OracleTypes.VARCHAR);
			call.registerOutParameter(3, OracleTypes.NUMBER);
			call.registerOutParameter(4, OracleTypes.VARCHAR);
			
			//执行
			call.execute();
			
			//输出
			String name = call.getString(2);
			double sal = call.getDouble(3);
			String job = call.getString(4);
			
			System.out.println(name+"\t"+sal+"\t"+job);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.release(conn, call, null);
		}
	}

/*
 * create or replace function queryEmpIncome(eno in number) 
return number
 */
	@Test
	public void testFunction(){
		//{?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
		String sql = "{?=call queryEmpIncome(?)}";
		
		Connection conn = null;
		CallableStatement call = null;
		try {
			conn = JDBCUtils.getConnection();
			call = conn.prepareCall(sql);
			
			call.registerOutParameter(1, OracleTypes.NUMBER);
			call.setInt(2, 7839);
			
			//执行
			call.execute();
			
			//取出年收入
			double income = call.getDouble(1);
			
			System.out.println(income);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.release(conn, call, null);
		}		
	}


	@Test
	public void testCursor(){
		String sql = "{call mypackage.QUERYEMPLIST(?,?)}";
		
		Connection conn = null;
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			call = conn.prepareCall(sql);
			
			//对于in参数，赋值
			call.setInt(1,20);
			
			//对于out参数，申明
			call.registerOutParameter(2, OracleTypes.CURSOR);
			
			//执行
			call.execute();
			
			//取出结果
			rs = ((OracleCallableStatement)call).getCursor(2);
			while(rs.next()){
				//取出一个员工
				String name = rs.getString("ename");
				double sal = rs.getDouble("sal");
				System.out.println(name+"\t"+sal);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.release(conn, call, rs);
		}		
		
	}
}














