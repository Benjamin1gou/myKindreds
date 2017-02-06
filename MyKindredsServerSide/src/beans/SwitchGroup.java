/***************************
 * ClassName: SwitchGroup
 * Creater: Tester
 * Createdate: 2017/02/03
 * content: DB接続を行いGroupに関する情報を取得追加検索を行う
 * *************************/
package beans;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;

import beans.DBManager.PreparedStatementByKoki;
import common.Database;


public class SwitchGroup {

	private String _groupName;
	private String _userId;
	private HttpServlet _servlet;
	private String _type;
	private ArrayList<HashMap<String, String>>result;
	private ArrayList<ArrayList<String>>list;
	
	public SwitchGroup(){
		
	}
	
	public SwitchGroup(String groupName, String userId, String type, HttpServlet servlet){
		this._groupName = groupName;
		this._userId = userId;
		this._type = type;
		this._servlet = servlet;
	}
	
	public ArrayList<HashMap<String, String>> typeSwitch(){
		result = new ArrayList<HashMap<String, String>>();
		switch(_type){
		case "GADD":
			typeGadd();
			break;
		case "GSEARCH":
			typeGsearch();
			break;
		case "GLIST":
			typeGlist();
			break;
		default:
			typeDefault();
			break;
		}
		return result;
	}
	
	private void typeGadd(){
		try{
			//id取得部分
			DBManager db = new DBManager(Database.DBName);
			PreparedStatementByKoki statementByKoki=null;
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet,"GetMaxId.sql"));
//			statementByKoki.setString("TABLE_NAME", "group");
			list = statementByKoki.select();
			int maxIdNo =0;
			if(null != list.get(0).get(0)){
				maxIdNo = Integer.parseInt(list.get(0).get(0));
			}
			System.out.println("maxIdNo:"+maxIdNo);
			
			
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet,"GroupAdd.sql"));
			statementByKoki.setInt("_ID", maxIdNo+1);
			statementByKoki.setString("_TITLE", _groupName);
			statementByKoki.update();
			
			//行番号取得部分
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet,"GetGroupLine.sql"));
			statementByKoki.setInt("USER_ID", maxIdNo+1);
			list = statementByKoki.select();
			int maxLineNo =0;
			if(null != list.get(0).get(0)){
				maxLineNo = Integer.parseInt(list.get(0).get(0));
			}
			
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet, "GroupDetailAdd.sql"));
			statementByKoki.setInt("_ID", maxIdNo+1);
			statementByKoki.setInt("_LINE", maxLineNo+1);
			statementByKoki.setString("_USER", _userId);
			
			int errorNum = statementByKoki.update();
			HashMap<String, String> x = new HashMap<String,String>();
			if(errorNum == 1){
				x.put("groupName", "成功しました");
			}else{
				x.put("groupName", "失敗しました");
			}
			result.add(x);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void typeGsearch(){
		try{
			DBManager db = new DBManager(Database.DBName);
			PreparedStatementByKoki statementByKoki=null;
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet,"GroupSearch.sql"));
			statementByKoki.setString("GROUP_NAME", _groupName);
			list = statementByKoki.select();
			HashMap<String, String> browseData = new HashMap<String, String>();
			for(ArrayList<String> row: list){
				browseData = new HashMap<String, String>();
				browseData.put("groupName", row.get(0));
				result.add(browseData);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void typeGlist(){
		try{
			DBManager db = new DBManager(Database.DBName);
			PreparedStatementByKoki statementByKoki=null;
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet,"GroupUserList.sql"));
			statementByKoki.setString("USER_ID", _userId);
			list = statementByKoki.select();
			HashMap<String, String> browseData = new HashMap<String, String>();
			for(ArrayList<String> row: list){
				browseData = new HashMap<String, String>();
				browseData.put("groupName", row.get(0));
				result.add(browseData);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void typeDefault(){
		HashMap<String, String> defaultData = new HashMap<String, String>();
		defaultData.put("groupName", "error: typeDefaultException");
		result.add(defaultData);
	}
	
}








