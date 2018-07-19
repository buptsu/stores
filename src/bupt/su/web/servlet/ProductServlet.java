package bupt.su.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bupt.su.constant.Constant;
import bupt.su.domain.PageBean;
import bupt.su.domain.Product;
import bupt.su.service.ProductService;
import bupt.su.service.implement.ProductServiceImplement;
import bupt.su.utils.BeanFactory;

/**
 * Servlet implementation class ProductServlet
 */
public class ProductServlet extends BaseServlet {

/**
 * 通过商品id获取商品
 * @param request
 * @param response
 * @return
 * @throws ServletException
 * @throws IOException
 * @throws SQLException
 */
	public String getProductById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//获取商品id
		String productId = (String) request.getParameter("productId");
		//调用service 返回product
		ProductService ps = (ProductService) BeanFactory.getBean("ProductService");;
		Product p=ps.getProductById(productId);
		// 放入request域
		request.setAttribute("product", p);
		//请求转发
		return "/jsp/product_info.jsp";

	}
	/**
	 * 获取商品分类 及当前页来获取商品
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String findByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//获取商品分类及当前页
		String cid = (String) request.getParameter("cid");
		int currPage = Integer.parseInt(request.getParameter("currPage"));
		//调用service 返回product
		ProductService ps = (ProductService) BeanFactory.getBean("ProductService");;
		PageBean<Product> list=ps.findProductByPage(cid,currPage,Constant.PAGE_SIZE);
		// 放入request域
		request.setAttribute("pageBean", list);
		//请求转发
		return "/jsp/product_list.jsp";

	}


}
