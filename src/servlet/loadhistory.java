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

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;

import database.conn.ConnectionUtils;
import servlet.cart.Order;

import utils.myUtils;

/**
 * Servlet implementation class loadhistory
 */
@WebServlet("/loadhistory")
public class loadhistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loadhistory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
        String sql = "Select a.email, a.item, a.price from items a "
                + " where a.user = ?";
        try {
		    PreparedStatement pstm;
			pstm = conn.prepareStatement(sql);
            pstm.setString(1, "Abhinav");
            ResultSet rs = pstm.executeQuery();
            while(rs.next())
            {
            String item=rs.getString(1);
            String usertable="Abhinav";
            int price=rs.getInt(3);
            System.out.println("usertable"+usertable);
            History a=new History(item,price);
            System.out.println("creating instace");
            HazelcastInstance instance;
            System.out.println("after instace");
            ServletContext servletContext = request.getServletContext();
    		instance = (HazelcastInstance)servletContext.getAttribute("hcastInstance");
    		MultiMap<String, History> historyMap = instance.getMultiMap("history");
            historyMap.put(usertable, a);
            response.setContentType("text/html");
	        System.out.println(a.item); 
	        
            }
            
	        
	        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}         

	

		
	    System.out.println("in history servlet");
		HazelcastInstance instance;
		ServletContext servletContext = request.getServletContext();
		instance = (HazelcastInstance)servletContext.getAttribute("hcastInstance");
	    
		   	MultiMap<String, History> historyMap = instance.getMultiMap("history");
	    	System.out.println ("mapCustomers.size() "+ historyMap.size());
	    	System.out.println ("mapCustomers.size() "+ historyMap);
	    	System.out.println ("mapCustomers.size() "+ historyMap);
	    	StringBuffer historyData = new StringBuffer();
	        
	        for (History s: historyMap.get("Abhinav")){
	        	historyData.append(s.item);
	        	historyData.append(" ");
	        	historyData.append(s.price);
	        	historyData.append(" ");
	        }
	        System.out.println(historyData.toString());
	        response.setContentType("text/html");
	        
	        response.getWriter().write(historyData.toString());
	    }

	
	@SuppressWarnings("serial")
	public class History implements Serializable { 
    	String item; 
    	int price;
    	public History(String item, int price){
    		this.item=item;
    		this.price=price;
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
