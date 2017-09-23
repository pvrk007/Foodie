package servlet;
import servlet.*;
import servlet.cart.Order;
import utils.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;

import database.conn.ConnectionUtils;
/**
 * Servlet implementation class order
 */
@WebServlet("/order")
public class order extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String s;    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public order() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
		System.out.println("in order post method");
		ServletContext servletContext = request.getServletContext();
		HazelcastInstance instance = (HazelcastInstance)servletContext.getAttribute("hcastInstance");
		System.out.println("in cart servlet hazelcastinstance" + instance);
		
		MultiMap<String, Order> cartMap = instance.getMultiMap("cart");
		 System.out.println ("mapCustomers.size() "+ cartMap.size());
	        int i= 0;
	        for (Order s: cartMap.get("Abhinav")){
	        	System.out.println("item" + ++i +": " + s.order);
			Connection conn = myUtils.getStoredConnection (request);
			if (conn == null){
				try{
					conn = ConnectionUtils.getConnection ();
				}
				catch (Exception e){
					System.out.println ("Connnection failed");
					return;
				}
			}			
		    String query = " insert into items(email, user, item, price, quantity)" + " values (?, ?, ?, ?, ?)";
		    try {
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		      preparedStmt.setString (1, s.order);
		      preparedStmt.setString (2, "Abhinav");
		      preparedStmt.setString (3, s.order);
		      preparedStmt.setLong (4, s.price);
		      preparedStmt.setLong (5, s.price);
		      preparedStmt.execute();
		      conn.close();
		    }
		    catch (Exception e){
		    	System.out.println("Exception");
		    }
		    
	        }
	        cartMap.remove("Abhinav");
		/*User U= new User("Abhinav",item);

			Itemstore I=new Itemstore();
		//	I.Itemstore();
			I.store1(user,U);
		*/
		
		
		//doGet(request, response);
	}
}

