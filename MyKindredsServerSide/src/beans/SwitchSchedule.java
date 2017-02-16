/***************************
 * ClassName: SwitchSchedule
 * Creater: Tester
 * Createdate: 2017/02/13
 * content: ユーザの予定を操作するクラス
 * *************************/
package beans;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;

import beans.DBManager.PreparedStatementByKoki;
import common.Database;

public class SwitchSchedule {

	/**
	 * @auther Tester
	 * 2017/02/13
	 * @param _userId String ユーザーID
	 */
	private String _userId;
	
	/**
	 * @auther Tester
	 * 2017/02/13
	 * @param _servlet HttpServlet 使用servlet
	 */
	private HttpServlet _servlet;
	
	/**
	 * @auther Tester
	 * 2017/02/13
	 * @param _type String 分岐名
	 */
	private String _type;
	
	/**
	 * @auther Tester
	 * 2017/02/13
	 * @param _date String 予定日
	 */
	private String _date;
	
	/**
	 * @auther Tester
	 * 2017/02/13
	 * @param _schedule String　予定内容
	 */
	private String _schedule;
	
	/**
	 * @auther Tester
	 * 2017/02/13
	 * @param result ArrayList<HashMap<String,String>>　返却用
	 */
	private ArrayList<HashMap<String, String>> result;
	
	/**
	 * @auther Tester
	 * 2017/02/13
	 * @param list ArrayList<ArrayList<String>>　抽出用
	 */
	private ArrayList<ArrayList<String>> list;
	
	private int line = 0;
	
	public SwitchSchedule(){
		
	}
	
	public SwitchSchedule(String userId, String type,String date, String schedule, HttpServlet servlet){
		this._userId = userId;
		this._type = type;
		this._date = date;
		this._schedule = schedule;
		this._servlet = servlet;
	}
	
	public ArrayList<HashMap<String, String>> typeSwitch(){
		result = new ArrayList<HashMap<String, String>>();
		switch(_type){
		case "ADD":
			typeAdd();
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
	 * 予定追加メソッド
	 */
	private void typeAdd(){
		try{
			int maxLineNo = 0;
			maxLineNo = DbCommons.getMaxLine(_userId, _servlet, "GetScheduleLine.sql");
			
			DBManager db = new DBManager(Database.DBName);
			PreparedStatementByKoki statementByKoki=null;
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet,"ScheduleInsert.sql"));
			statementByKoki.setInt("USER_ID", Integer.parseInt(_userId));
			statementByKoki.setString("DATE", _date);
			statementByKoki.setInt("LINE", maxLineNo+1);
			statementByKoki.setString("CONTENT", _schedule);
			
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
	
	private void typeBrowse(){
		System.out.println(_date);
		try{
			DBManager db = new DBManager(Database.DBName);
			PreparedStatementByKoki statementByKoki=null;
			statementByKoki = db.getStatementByKoki(InspectionValue.readSql(_servlet,"ScheduleBrowse.sql"));
			statementByKoki.setInt("USER_ID", Integer.parseInt(_userId));
			statementByKoki.setString("_DATE", _date);
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
	
	private void typeDefault(){
		HashMap<String, String> defaultData = new HashMap<String, String>();
		defaultData.put("title", "error: typeDefaultException");
		result.add(defaultData);
	}
}
