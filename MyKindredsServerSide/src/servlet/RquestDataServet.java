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
		HashMap<String, ArrayList<HashMap<String,String>>> returnData = new HashMap<String, ArrayList<HashMap<String,String>>>();
		
		
		JSON json = new JSON();
		switch(method){
		case "Todo":
			String type = request.getParameter("type");
			String mission = request.getParameter("mission");
			SwitchToDo todo = new SwitchToDo(mission, userId, type, this);
			returnData.put("data", todo.typeSwitch());
			break;
		case "":
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
