/***************************
 * ClassName: RequestDataServlet
 * CreateDate: 2017/01/19
 * Creater: Tester
 * content: Android端末よりリクエストが来た際にDBより必要な情報を返却するサーブレット
 * *************************/
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.SwitchGroup;
import beans.SwitchMemo;
import beans.SwitchToDo;
import net.arnx.jsonic.JSON;

/**
 * Servlet implementation class RquestDataServet
 */
@WebServlet("/RquestDataServet")
public class RquestDataServet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RquestDataServet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String method = request.getParameter("method");
		String userId = request.getParameter("userId");
		String type = "";
		String mission = "";
		HashMap<String, ArrayList<HashMap<String,String>>> returnData = new HashMap<String, ArrayList<HashMap<String,String>>>();
		System.out.println("method:"+method);
		System.out.println("userId:"+userId);
		JSON json = new JSON();
		switch(method){
		case "Todo":
			type = request.getParameter("type");
			mission = request.getParameter("mission");
			int Line = 0;
			
			System.out.println("type:"+type);
			System.out.println("mission:"+mission);
			
			SwitchToDo todo = new SwitchToDo(mission, userId, type, this);
			if("DELETE".equals(type)){
				Line = Integer.parseInt(request.getParameter("line"));
				todo.setLine(Line);
			}
			returnData.put("data", todo.typeSwitch());
			break;
		case "Memo":
			type = request.getParameter("type");
			mission = request.getParameter("mission");
			System.out.println("type:"+type);
			System.out.println("mission:"+mission);
			SwitchMemo memo = new SwitchMemo(mission, userId, type, this);
			returnData.put("data", memo.typeSwitch());
			
		case "Start":
			SwitchToDo startTodo = new SwitchToDo("",userId, "BROWSE", this);
			returnData.put("todo", startTodo.typeSwitch());
			SwitchMemo startMemo = new SwitchMemo("", userId, "BROWSE", this);
			returnData.put("memo", startMemo.typeSwitch());
			break;
			
		case "Group":
			type = request.getParameter("type");
			String groupName = request.getParameter("groupName");
			System.out.println("type:"+type);
			System.out.println("groupName:"+groupName);
			SwitchGroup group = new SwitchGroup(groupName, userId, type, this);
			returnData.put("data", group.typeSwitch());
			break;
			
		case "Schedule":
			type = request.getParameter("type");
			String date = request.getParameter("date");
			String schedule = request.getParameter("schedule");
			
		}
		
		
		
		
		out.println(json.encode(returnData));
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
