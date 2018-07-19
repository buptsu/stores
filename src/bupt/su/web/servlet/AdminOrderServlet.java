package bupt.su.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bupt.su.constant.Constant;
import bupt.su.domain.Order;
import bupt.su.domain.OrderItem;
import bupt.su.domain.PageBean;
import bupt.su.service.OrderService;
import bupt.su.service.implement.OrderServiceImplement;
import bupt.su.utils.JsonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

/**
 * Servlet implementation class AdminOrderServlet
 */
public class AdminOrderServlet extends BaseServlet {

	/**
	 * @throws SQLException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public String findAllByState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException, InvocationTargetException, SQLException {
		// TODO Auto-generated method stub
		//1.接受state
		String state=request.getParameter("state");
		int currPage = Integer.parseInt(request.getParameter("currPage"));
		//2.调用service
		OrderService os=new OrderServiceImplement();
		PageBean<Order> pageBean=os.findAllByState(state, currPage, Constant.ADMIN_ORDER_PAGE_SIZE);
		
		//3.将list放入域中 请求转发
		request.setAttribute("pageBean", pageBean);
		if(state!=null){
			request.setAttribute("state", Integer.parseInt(state));
		}else{
			request.setAttribute("state", -1);
		}
		return "/admin/order/list.jsp";
	}

	/**
	 * 查询订单详情
	 * @throws SQLException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public String getDetailByOid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException, InvocationTargetException, SQLException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		//获取oid
		String oid = request.getParameter("oid");
		//调用service 获取订单详情 返回 order中的List<OrderItems>
		OrderService os=new OrderServiceImplement();
		List<OrderItem> list = os.findById(oid).getItems();
		//将list转换成json写回
		//排除不用写回的数据
		JsonConfig config = JsonUtil.configJson(new String[]{"class","itemid","order","pdate"});
		JSONArray jsonArray = JSONArray.fromObject(list, config);
		response.getWriter().print(jsonArray);
		System.out.println(jsonArray);
		return null;
	}
	/**
	 * 发货改变订单状态
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 */
	public String updateState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException, InvocationTargetException, SQLException {
		String oid = request.getParameter("oid");
		String state = request.getParameter("state");
		OrderService os = new OrderServiceImplement();
		Order order = os.findById(oid);
		order.setState(Integer.parseInt(state));
		os.updateOrder(order);
		response.sendRedirect(request.getContextPath()+"/adminOrder?method=findAllByState&state=1&currPage=1");
		return null;
		
	}

}
