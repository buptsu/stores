package bupt.su.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bupt.su.constant.Constant;
import bupt.su.domain.Category;
import bupt.su.domain.PageBean;
import bupt.su.domain.Product;
import bupt.su.service.ProductService;
import bupt.su.service.implement.CategoryServiceImplement;
import bupt.su.service.implement.ProductServiceImplement;

/**
 * Servlet implementation class AdminProductServlet
 */
public class AdminProductServlet extends BaseServlet {
	/**
	 * 
	 * 查询所有商品
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String  findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		// TODO Auto-generated method stub
		int currPage=Integer.parseInt(request.getParameter("currPage"));
		ProductService ps=new ProductServiceImplement();
		PageBean<Product> pageBean=ps.findAll(currPage,Constant.ADMIN_PRODUCT_PAGE_SIZE);
		request.setAttribute("pageBean", pageBean);
		return "/admin/product/list.jsp";
	}
	/**
	 * 跳转到添加商品的页面
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String  addUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//查询所有的分类  返回list （添加商品时选择分类要用）
		List<Category> list=new CategoryServiceImplement().findAllCategory();
		//list放入request域中
		request.setAttribute("clist", list);
		
		return "/admin/product/add.jsp";
	}
}
