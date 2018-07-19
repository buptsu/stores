package bupt.su.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bupt.su.domain.Category;
import bupt.su.service.CategoryService;
import bupt.su.service.implement.CategoryServiceImplement;
import bupt.su.utils.BeanFactory;
import bupt.su.utils.JsonUtil;

/**
 * 查询所有分类
 * Servlet implementation class CategoryServlet
 */
public class CategoryServlet extends BaseServlet {
	   public String findAllCategory(HttpServletRequest request,HttpServletResponse response) throws SQLException, IOException {
	    	//调用categoryService查询所有分类 返回值list
	    	CategoryService cs=(CategoryService) BeanFactory.getBean("CategoryService");;
	    	List<Category> list=cs.findAllCategory();
	    	//将返回的list放入到request域中
	    	String json=JsonUtil.list2json(list);
	    	//处理中文乱码，写回去
	    	response.setContentType("text/html;charset=utf-8");
	    	response.getWriter().print(json);
		   return null;
	   }

}
