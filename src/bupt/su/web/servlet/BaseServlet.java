package bupt.su.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用的servlet，实现找到实现了哪个servlet，并且调用子类servlet的方法。
 * Servlet implementation class BaseServlet
 */
public class BaseServlet extends HttpServlet {

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取子类
		try {
		Class clazz=this.getClass();
		System.out.println(clazz);
		//获取请求方法
		String m = request.getParameter("method");
		if(m==null){
			m="index";
		}
		System.out.println(m);
		//获取方法对象
		Method method = clazz.getMethod(m, HttpServletRequest.class,HttpServletResponse.class);
		//方法执行 返回值为请求路径
		String s=(String) method.invoke(this, request,response);
		
		//判断路径是否为空  不为空请求转发执行
		if(s!=null){
			request.getRequestDispatcher(s).forward(request, response);
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		} 
	}
	public String index(HttpServletRequest request, HttpServletResponse response) throws SQLException{
		return null;
	}

}