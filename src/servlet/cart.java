package servlet;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

import com.hazelcast.config.*;
import com.hazelcast.core.*;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.HazelcastClient;

/**
 * Servlet implementation class cart
 */
@WebServlet("/cart")
public class cart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public cart() {
        super();
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("deprecation")
	private HazelcastInstance getHConnection(){
    	ClientConfig clientConfig = new ClientConfig();
    	clientConfig.setLicenseKey("ENTERPRISE_HD#10Nodes#a6IO7KlwjbmNUAESkufVJ0F1HTr5y1411010191212016011910001119010");
        clientConfig.addAddress ("192.168.0.3:5701");
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        return client;
    }
  
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    System.out.println("in cart servlet");
		HazelcastInstance instance;
		String user = "Abhinav";
		ServletContext servletContext = request.getServletContext();
		instance = (HazelcastInstance)servletContext.getAttribute("hcastInstance");

	    if(request.getParameter("food")!=null)
	    {
		String item = request.getParameter("food");	
		int price = Integer.parseInt(request.getParameter("price"));
		
		
/*		try{
		instance = getHConnection();
		IMap<String, String> map = instance.getMap("cart");
        System.out.println("Map Size: " + map.size() + "Food: " + map.get("Abhinav"));
		}
		catch (Exception e){		
		Config cfg = new Config ();
		cfg.setLicenseKey("ENTERPRISE_HD#100Nodes#a6IO7KlwjbmNUAESkufVJ0F1HTr5y1411010191212016011910001119010");
		instance = Hazelcast.newHazelcastInstance(cfg);*/
		
		Order a=new Order(item,price);
		MultiMap<String, Order> cartMap = instance.getMultiMap("cart");
        cartMap.put(user, a);
        /*System.out.println ("mapCustomers.get() "+ cartMap.get("Abhinav"));*/
        
        int i= 0;
        for (Order s: cartMap.get("Abhinav")){
/*        	Collection <String > values = cartMap.get( s );*/
        	System.out.println("item" + ++i +": " + s.order+" "+s.price);
        }
	    }
	    else if (request.getParameter("loadcart")!=null)
	    {
	    	MultiMap<String, Order> cartMap = instance.getMultiMap("cart");
	    	System.out.println ("mapCustomers.size() "+ cartMap.size());
	    	StringBuffer cartData = new StringBuffer();
	        
	        for (Order s: cartMap.get("Abhinav")){
	        	cartData.append(s.order);
	        	cartData.append(" ");
	        	cartData.append(s.price);
	        	cartData.append(" ");
	        }
	        System.out.println(cartData.toString());
	        response.setContentType("text/html");
	        
	        response.getWriter().write(cartData.toString());
	    }
		/*}*/
}
	
	

	@SuppressWarnings("serial")
	public class Order implements Serializable { 
	    	String order; 
	    	int price;
	    	public Order(String order, int price){
	    		this.order=order;
	    		this.price=price;
	    	}
	     }
	   

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
