/***************************
 * ClassName:SwitchMemo
 * Creater: Tester
 * Createdate: 2017/02/02
 * content: DB接続をしメモに関する情報を取得または追加を行う。
 * *************************/
package beans;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;

public class SwitchMemo {
	
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
	
	/**
	 * @auther Tester
	 * 2017/02/02
	 * @param _type String 始動機能
	 */
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

	public SwitchMemo(){
		
	}
	
	public SwitchMemo(String mission, String userId, String type, HttpServlet servlet){
		this._misson = mission;
		this._servlet = servlet;
		this._userId = userId;
		this._type = type;
	}
	
	public ArrayList<HashMap<String, String>> typeSwitch(){
		result = new ArrayList<HashMap<String,String>>();
		switch(_type){
		case "INSERT":
			break;
		case "BROWSE":
			break;
		default:
			break;
		}
		return result;	
	}
	
}
