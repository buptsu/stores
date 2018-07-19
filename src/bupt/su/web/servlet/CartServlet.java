package bupt.su.web.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bupt.su.domain.Cart;
import bupt.su.domain.CartItem;
import bupt.su.domain.Product;
import bupt.su.service.implement.ProductServiceImplement;

/**
 * 购物车模块
 * Servlet implementation class CartServlet
 */
public class CartServlet extends BaseServlet {
	//从session域中获取购物车
	public Cart getCart(HttpServletRequest request){
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if(cart==null){
			cart =new Cart();
			request.getSession().setAttribute("cart", cart);
		}
		return cart;
	}
	public String add(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException  {
		// TODO Auto-generated method stub
		//获取商品id和商品数量
		String pid = request.getParameter("pid");
		int count= Integer.parseInt(request.getParameter("count"));
		//通过pid查询出商品对象
		Product product=new ProductServiceImplement().getProductById(pid);
		//拼装成购物车项catrItem 商品 （通过pid查询过来）商品数量（传递过来） 商品小计（自动计算）
		CartItem item = new CartItem(product, count);
		//添加到购物车中
		Cart cart = getCart(request);
		cart.add2Cart(item);
		//重定向
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		return null;
	}
	//从购物车中移除购物项
	public String remove(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//获取商品id
		String pid = request.getParameter("pid");
		//获取购物车
		getCart(request).removeFromCart(pid);
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		return null;
	}
	
	//清空购物车
	public String clear(HttpServletRequest request, HttpServletResponse response) throws IOException{
		getCart(request).clearCart();
		response.sendRedirect(request.getContextPath()+"/jsp/cart.jsp");
		return null;
	}
}
