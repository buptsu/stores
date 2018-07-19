package bupt.su.web.servlet;

import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bupt.su.constant.Constant;
import bupt.su.domain.Cart;
import bupt.su.domain.CartItem;
import bupt.su.domain.Order;
import bupt.su.domain.OrderItem;
import bupt.su.domain.PageBean;
import bupt.su.domain.User;
import bupt.su.service.OrderService;
import bupt.su.service.implement.OrderServiceImplement;
import bupt.su.utils.BeanFactory;
import bupt.su.utils.PaymentUtil;
import bupt.su.utils.UUIDUtils;

/**
 * Servlet implementation class OrderServlet
 */
public class OrderServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 生成订单
	 * @throws Exception 
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		//判断用户是否登陆
		User user = (User) request.getSession().getAttribute("user");
		if(user==null){
			request.setAttribute("msg", "请先登陆~");
			return "/jsp/msg.jsp";
		}
		//封装数据  订单id 订单时间   订单金额 当前用户 订单的所有订单项
		///订单id
		Order order =new Order();
		order.setOid(UUIDUtils.getId());
		///订单时间
		order.setOrdertime(new Date());
		///从session中获取cart,获取总金额
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		double total = cart.getTotal();
		order.setTotal(total);
		///设置用户
		order.setUser(user);
		///当前用户 订单的所有订单项
		/*
		 * 获取cart中的items
		 * 遍历items组装成orderItem
		 * 将orderItem添加到list中
		 */
		for (CartItem cartItem : cart.getItems()) {
			OrderItem orderItem = new OrderItem();
			
			orderItem.setItemid(UUIDUtils.getId());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setCount(cartItem.getCount());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setOrder(order);
			
			//添加到list
			order.getItems().add(orderItem);
		}
		//调用service添加订单
		OrderService service=(OrderService) BeanFactory.getBean("OrderService");;
		service.add(order);
		//将order放入request域中
		request.setAttribute("order", order);
		//清空购物车
		request.getSession().removeAttribute("cart");
		//请求转发
		return "/jsp/order_info.jsp";
	}
	
	/**
	 *分页查询出当前用户的所有订单 封装成pageBean的形式
	 */
	public String findAllByPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//从session中获取当前用户
		User user = (User) request.getSession().getAttribute("user");
		if(user==null){
			request.setAttribute("msg", "请先登陆~");
			return "/jsp/msg.jsp";
		}
		// 从请求参数中获取查询的订单页currPage
		int currPage = Integer.parseInt(request.getParameter("currPage"));
		//设置每页显示的订单数
		int pageSize=Constant.ORDER_PAGE_SIZE;
		//根据用户调用service方法查询出订单列表 返回值为 PageBean
		OrderService os = (OrderService) BeanFactory.getBean("OrderService");
		PageBean<Order> pageBean=os.findAllByPage(user,currPage,pageSize);

		//将返回值放到request域中
		request.setAttribute("pageBean", pageBean);
		//请求转发
		return "/jsp/order_list.jsp";
		
	}
	
	/**
	 * 根据订单id查询出订单详情
	 */
	public String findById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String oid = request.getParameter("oid");
		BeanFactory.getBean("OrderService");
		OrderService os = (OrderService) BeanFactory.getBean("OrderService");
		Order order=os.findById(oid);
		request.setAttribute("order", order);
		return "/jsp/order_info.jsp";
	}
	/**
	 * 付款并完成订单更新
	 */
	public String pay(HttpServletRequest request,HttpServletResponse respone) throws Exception{
		//接受参数
		String address=request.getParameter("address");
		String name=request.getParameter("name");
		String telephone=request.getParameter("telephone");
		String oid=request.getParameter("oid");
		
		
		//通过id获取order
		OrderService s=(OrderService) BeanFactory.getBean("OrderService");;
		Order order = s.findById(oid);
		
		order.setAddress(address);
		order.setName(name);
		order.setTelephone(telephone);
		
		//更新order
		s.updateOrder(order);
		

		// 组织发送支付公司需要哪些数据
		String pd_FrpId = request.getParameter("pd_FrpId");
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = oid;
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("responseURL");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt,
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP,
				pd_FrpId, pr_NeedResponse, keyValue);
	
		
		//发送给第三方
		StringBuffer sb = new StringBuffer("https://www.yeepay.com/app-merchant-proxy/node?");
		sb.append("p0_Cmd=").append(p0_Cmd).append("&");
		sb.append("p1_MerId=").append(p1_MerId).append("&");
		sb.append("p2_Order=").append(p2_Order).append("&");
		sb.append("p3_Amt=").append(p3_Amt).append("&");
		sb.append("p4_Cur=").append(p4_Cur).append("&");
		sb.append("p5_Pid=").append(p5_Pid).append("&");
		sb.append("p6_Pcat=").append(p6_Pcat).append("&");
		sb.append("p7_Pdesc=").append(p7_Pdesc).append("&");
		sb.append("p8_Url=").append(p8_Url).append("&");
		sb.append("p9_SAF=").append(p9_SAF).append("&");
		sb.append("pa_MP=").append(pa_MP).append("&");
		sb.append("pd_FrpId=").append(pd_FrpId).append("&");
		sb.append("pr_NeedResponse=").append(pr_NeedResponse).append("&");
		sb.append("hmac=").append(hmac);
		
		respone.sendRedirect(sb.toString());
		
		return null;
	}
	
	public String callback(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String rb_BankId = request.getParameter("rb_BankId");
		String ro_BankOrderId = request.getParameter("ro_BankOrderId");
		String rp_PayDate = request.getParameter("rp_PayDate");
		String rq_CardNo = request.getParameter("rq_CardNo");
		String ru_Trxtime = request.getParameter("ru_Trxtime");
		// 身份校验 --- 判断是不是支付公司通知你
		String hmac = request.getParameter("hmac");
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString(
				"keyValue");

		// 自己对上面数据进行加密 --- 比较支付公司发过来hamc
		boolean isValid = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd,
				r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid,
				r8_MP, r9_BType, keyValue);
		if (isValid) {
			// 响应数据有效
			if (r9_BType.equals("1")) {
				// 浏览器重定向
				System.out.println("111");
				request.setAttribute("msg", "您的订单号为:"+r6_Order+",金额为:"+r3_Amt+"已经支付成功,等待发货~~");
				
			} else if (r9_BType.equals("2")) {
				// 服务器点对点 --- 支付公司通知你
				System.out.println("付款成功！222");
				// 修改订单状态 为已付款
				// 回复支付公司
				response.getWriter().print("success");
			}
			
			//修改订单状态
			OrderService s=(OrderService) BeanFactory.getBean("OrderService");;
			Order order = s.findById(r6_Order);
			order.setState(1);
			
			s.updateOrder(order);
			
		} else {
			// 数据无效
			System.out.println("数据被篡改！");
		}
		
		
		return "/jsp/msg.jsp";
		
	}
	/**
	 * 确认收货
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateState(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String oid = request.getParameter("oid");
		String state = request.getParameter("state");
		OrderService os = (OrderService) BeanFactory.getBean("OrderService");;
		Order order = os.findById(oid);
		order.setState(Integer.parseInt(state));
		os.updateOrder(order);
		response.sendRedirect(request.getContextPath()+"/order?method=findAllByPage&currPage=1");
		return null;
	}
}
