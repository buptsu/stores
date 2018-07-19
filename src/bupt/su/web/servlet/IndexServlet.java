package bupt.su.web.servlet;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bupt.su.domain.Category;
import bupt.su.domain.Product;
import bupt.su.service.CategoryService;
import bupt.su.service.ProductService;
import bupt.su.service.implement.CategoryServiceImplement;
import bupt.su.service.implement.ProductServiceImplement;

/**
 * 
 * Servlet implementation class IndexServlet
 */
public class IndexServlet extends BaseServlet {
    public String index(HttpServletRequest request,HttpServletResponse response) throws SQLException {
    	//从数据库查询商品分类信息，放到qequest域中，并跳转到首页（但只是实现了index页面上的商品信息的展示）

    	/*    	//调用categoryService查询所有分类 返回值list
    	CategoryService cs=new CategoryServiceImplement();
    	List<Category> list=cs.findAllCategory();
    	//将返回的list放入到request域中
    	request.setAttribute("categoryList", list);
    	*/
    	//从数据库中查询最新商品和热门商品
    	ProductService ps=new ProductServiceImplement();
    	//最新商品
    	List<Product> newList=ps.findNewProduct();
    	List<Product> hotList=ps.findHotProduct();
    	//将返回的list放入request域中
    	request.setAttribute("newList", newList);
    	request.setAttribute("hotList", hotList);
    	//请求转发
    	return "/jsp/index.jsp";
    }

}
