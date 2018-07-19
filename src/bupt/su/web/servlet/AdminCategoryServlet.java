package bupt.su.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bupt.su.domain.Category;
import bupt.su.service.CategoryService;
import bupt.su.service.ProductService;
import bupt.su.service.implement.CategoryServiceImplement;
import bupt.su.service.implement.ProductServiceImplement;
import bupt.su.utils.UUIDUtils;

/**
 * Servlet implementation class AdminCategoryServlet
 */
public class AdminCategoryServlet extends BaseServlet {
	
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		// TODO Auto-generated method stub
		//调用categoryservice查询所有分类信息 返回值为list
		List<Category> list = new CategoryServiceImplement().findAllCategory();
		request.setAttribute("list", list);
		return "/admin/category/list.jsp";
	}
	/**
	 * 跳转到添加页面上
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String addUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		return "/admin/category/add.jsp";
	}
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//接收cname
		String cname = request.getParameter("cname");
		//封装category
		Category category = new Category();
		category.setCname(cname);
		category.setCid(UUIDUtils.getId());
		//调用service完成添加操作
		CategoryService cs=new CategoryServiceImplement();
		cs.add(category);
		//重定向，查询所有分类
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=findAll");
		return null;
	}
	
	public String getById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		String cid = request.getParameter("cid");
		CategoryService cs=new CategoryServiceImplement();
		Category cg=cs.getById(cid);
		request.setAttribute("category", cg);
		return "/admin/category/edit.jsp";
	}
	public String update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//获取cid 和要更新的cname
		String cid = request.getParameter("cid");
		String cname = request.getParameter("cname");
		
		Category c=new Category();
		c.setCid(cid);
		c.setCname(cname);
		
		CategoryService cs=new CategoryServiceImplement();
		cs.update(c);
		//重定向，查询所有分类
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=findAll");
		return null;
	}
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		//获取cid
		String cid = request.getParameter("cid");
		//删除该分类
		CategoryService cs =new CategoryServiceImplement();
		cs.delete(cid);
		//重定向，查询所有分类
		response.sendRedirect(request.getContextPath()+"/adminCategory?method=findAll");
		return null;
	}
}
	
