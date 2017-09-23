package servlet;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;

import database.userAccount;
import database.conn.ConnectionUtils;
import utils.DBUtils;
import utils.myUtils;


/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userName = request.getParameter("username");
        String password = request.getParameter("password");
        boolean hasError = false;
        String errorString = null;
        userAccount user = null;
        if (userName == null || password == null
                || userName.length() == 0 || password.length() == 0) {
           hasError = true;
           errorString = "Required username and password!";
       } else {
           Connection conn = myUtils.getStoredConnection(request);
           if (conn == null)
             {
           	try{
           	conn = ConnectionUtils.getConnection();	
           	}
           	catch (Exception e){
           		System.out.println ("Connection failed");
           	}
           	System.out.println ("conn: " +conn);
             }
           try {
             
               user = DBUtils.findUser(conn, userName, password);
                
               if (user == null) {
                   hasError = true;
                   errorString = "User Name or password invalid";
               }
           } catch (SQLException e) {
               e.printStackTrace();
               hasError = true;
               errorString = e.getMessage();
           }
       }
        if (hasError) {
            user = new userAccount();
            user.setUserName(userName);
            user.setPassword(password);
            // Store information in request attribute, before forward.
            request.setAttribute("errorString", errorString);
            request.setAttribute("user", user) ;
            response.setContentType("text/html");   
	        response.sendRedirect(request.getContextPath() + "/user.html");
        }
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		doGet(request, response);
	}

}
