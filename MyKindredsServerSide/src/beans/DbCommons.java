/***************************
 * ClassName:DbCommons
 * Creater: Tester
 * Createdate: 2017/02/13
 * content: DB接続をする際のなんんども使う細々したメソッドたちがいる
 * *************************/
package beans;

import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

import beans.DBManager.PreparedStatementByKoki;
import common.Database;

public class DbCommons {
	
	
	
	public static int getMaxLine(String _userId, HttpServlet _servlet, String sqlName){
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		int maxLineNo =0;
		try{
			//行番号取得部分
			DBManager db = new DBManager(Database.DBName);
			PreparedStatementByKoki statementByKoki=null;
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet, sqlName));
			statementByKoki.setString("USER_ID", _userId);
			list = statementByKoki.select();
			
			if(null != list.get(0).get(0)){
				maxLineNo = Integer.parseInt(list.get(0).get(0));
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return maxLineNo;
	}

}
