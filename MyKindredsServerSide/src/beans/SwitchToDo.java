/***************************
 * ClassName: SwitchTodo
 * CreateDate: 2017/01/19
 * Creater: Tester
 * content: DB接続をしToDoに関する情報を取得または追加、削除を行う。
 * *************************/
package beans;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;

import beans.DBManager.PreparedStatementByKoki;
import common.Database;

public class SwitchToDo {
	
	/**
	 * @auther Tester
	 * 2017/01/20
	 * @param _misson String 登録内容
	 */
	private String _misson;
	
	/**
	 * @auther Tester
	 * 2017/01/22
	 * @param _userId String ユーザーID
	 */
	private String _userId;
	
	/**
	 * @auther Tester
	 * 2017/01/22
	 * @param _servlet HttpServlet 使用servlet
	 */
	private HttpServlet _servlet;
	
	private String _type;
	
	/**
	 * @auther Tester
	 * 2017/01/22
	 * @param result ArrayList<HashMap<String,String>>　返却用
	 */
	private ArrayList<HashMap<String, String>> result;
	
	/**
	 * @auther Tester
	 * 2017/01/22
	 * @param list ArrayList<ArrayList<String>>　抽出用
	 */
	private ArrayList<ArrayList<String>> list;
	
	 private int line = 0;
	
	
	
	public SwitchToDo(){
		
	}
	
	public SwitchToDo(String mission,String userId , String type, HttpServlet servlet){
		this._misson = mission;
		this._servlet = servlet;
		this._userId = userId;
		this._type = type;
	}
	
	public void setLine(int line){
		this.line = line;
	}

	public ArrayList<HashMap<String, String>> typeSwitch(){
		result = new ArrayList<HashMap<String, String>>();
		switch (_type) {
		case "INSERT":
			typeInsert();
			break;
		case "DELETE":
			typeDelete();
			break;
		case "BROWSE":
			typeBrowse();
			break;
		default:
			typeDefault();
			break;
		}
		return result;
	}
	
	
	/**
	 * Todo情報をDBに登録するメソッド
	 * @return 成功メッセージorエラーメッセージ
	 */
	public void typeInsert(){
		try{
			//行番号取得部分
			DBManager db = new DBManager(Database.DBName);
			PreparedStatementByKoki statementByKoki=null;
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet,"GetTodoLine.sql"));
			statementByKoki.setString("USER_ID", _userId);
			list = statementByKoki.select();
			int maxLineNo =0;
			if(null != list.get(0).get(0)){
				maxLineNo = Integer.parseInt(list.get(0).get(0));
			}
			
			
			//データ登録部分
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet,"TodoInsert.sql"));
			statementByKoki.setInt("_ID", Integer.parseInt(_userId));
			statementByKoki.setInt("LINE", maxLineNo+1);
			statementByKoki.setString("TITLE", _misson);
			int errorNum = statementByKoki.update();
			HashMap<String, String> x = new HashMap<String,String>();
			if(errorNum == 1){
				x.put("title", "成功しました");
			}else{
				x.put("title", "失敗しました");
			}
			result.add(x);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * ユーザ情報を元に登録されているTodo情報を取得するメソッド
	 */
	public void typeBrowse(){
		try{
			DBManager db = new DBManager(Database.DBName);
			PreparedStatementByKoki statementByKoki=null;
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet,"TodoBrowse.sql"));
			statementByKoki.setInt("_ID", Integer.parseInt(_userId));
			list = statementByKoki.select();
			HashMap<String, String> browseData = new HashMap<String, String>();
			for(ArrayList<String> row: list){
				browseData = new HashMap<String, String>();
				browseData.put("id", row.get(0));
				browseData.put("title", row.get(1));
				result.add(browseData);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void typeDelete(){
		try{
			DBManager db = new DBManager(Database.DBName);
			PreparedStatementByKoki statementByKoki=null;
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet,"TodoDelete.sql"));
			statementByKoki.setString("_ID", _userId);
			statementByKoki.setInt("_LINE", line);
			int errorNum = statementByKoki.update();
			HashMap<String, String> x = new HashMap<String,String>();
			if(errorNum == 1){
				x.put("title", "削除成功しました");
			}else{
				x.put("title", "失敗しました");
			}
			result.add(x);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void typeDefault(){
		HashMap<String, String> defaultData = new HashMap<String, String>();
		defaultData.put("title", "error: typeDefaultException");
		result.add(defaultData);
	}
}














